package com.flint.presentation.onboarding

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.navigation.Route
import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.repository.AuthRepository
import com.flint.domain.repository.SearchRepository
import com.flint.domain.repository.StorageRepository
import com.flint.domain.repository.TermsRepository
import com.flint.domain.repository.UserRepository
import com.flint.domain.type.FileExtension
import com.flint.domain.type.OttType
import com.flint.domain.type.StoragePathType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel
@Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val termsRepository: TermsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val tempToken: String = savedStateHandle.toRoute<Route.OnboardingGraph>().tempToken

    private val _uiState = MutableStateFlow(OnboardingProfileUiState())
    val uiState: StateFlow<OnboardingProfileUiState> = _uiState.asStateFlow()

    private val _contentUiState = MutableStateFlow(OnboardingContentUiState())
    val contentUiState: StateFlow<OnboardingContentUiState> = _contentUiState.asStateFlow()

    private val _ottUiState = MutableStateFlow(OnboardingOttUiState())
    val ottUiState: StateFlow<OnboardingOttUiState> = _ottUiState.asStateFlow()

    private val _termsUiState = MutableStateFlow(OnboardingTermsUiState())
    val termsUiState: StateFlow<OnboardingTermsUiState> = _termsUiState.asStateFlow()

    private val _signupUiState = MutableStateFlow(OnboardingSignupUiState())
    val signupUiState: StateFlow<OnboardingSignupUiState> = _signupUiState.asStateFlow()

    private var searchJob: Job? = null

    // ---------- onboarding terms ----------
    fun loadTerms() {
        if (_termsUiState.value.termsState is UiState.Loading ||
            _termsUiState.value.termsState is UiState.Success
        ) return

        viewModelScope.launch {
            _termsUiState.update { it.copy(termsState = UiState.Loading) }
            termsRepository.getTermsList()
                .onSuccess { terms ->
                    _termsUiState.update { it.copy(termsState = UiState.Success(terms)) }
                }
                .onFailure { error ->
                    _termsUiState.update { it.copy(termsState = UiState.Failure) }
                    Timber.e(error, "Failed to load terms")
                }
        }
    }

    fun agreeToTerms(termIds: List<String>) {
        _termsUiState.update { it.copy(agreedTermsIds = termIds) }
    }

    // ---------- onboarding profile ----------
    fun updateNickname(nickname: String) {
        if (nickname.length <= OnboardingProfileUiState.MAX_LENGTH) {
            val isFormatValid = OnboardingProfileUiState.isValidFormat(nickname)
            _uiState.update { currentState ->
                currentState.copy(
                    nickname = nickname,
                    isValid = nickname.length >= OnboardingProfileUiState.MIN_LENGTH,
                    isFormatValid = isFormatValid,
                    isNicknameAvailable = null,
                    nicknameErrorType = when {
                        !isFormatValid && nickname.isNotEmpty() -> NicknameErrorType.INVALID_FORMAT
                        currentState.nicknameErrorType == NicknameErrorType.DUPLICATE -> NicknameErrorType.DUPLICATE
                        else -> null
                    },
                )
            }
        }
    }

    fun checkNicknameDuplication() {
        val currentNickname = _uiState.value.nickname

        // 형식이 유효하지 않으면 중복 체크 실행하지 않음
        if (!_uiState.value.isFormatValid) {
            return
        }

        viewModelScope.launch {
            userRepository.checkNickname(currentNickname).onSuccess { result ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isNicknameAvailable = result.isAvailable,
                        nicknameErrorType = if (!result.isAvailable) {
                            NicknameErrorType.DUPLICATE
                        } else {
                            null
                        },
                    )
                }
            }
        }
    }

    fun clearNicknameError() {
        _uiState.update { currentState ->
            currentState.copy(nicknameErrorType = null)
        }
    }

    fun updateProfileImage(uri: Uri?) {
        _uiState.update { it.copy(profileImageUri = uri) }
    }

    // ---------- onboarding content ----------
    fun updateSearchKeyword(keyword: String) {
        _contentUiState.update { currentState ->
            currentState.copy(searchKeyword = keyword)
        }
    }

    fun loadInitialContents() {
        getSearchContentList(keyword = null, genre = null)
    }

    fun searchContents() {
        val keyword = _contentUiState.value.searchKeyword.ifEmpty { null }
        val genre = _contentUiState.value.selectedGenre
        getSearchContentList(keyword = keyword, genre = genre)
    }

    fun selectGenre(genre: String) {
        _contentUiState.update { currentState ->
            val newSelected = if (currentState.selectedGenre == genre) null else genre
            currentState.copy(selectedGenre = newSelected)
        }
        // 장르 선택/해제 시 즉시 재검색
        val keyword = _contentUiState.value.searchKeyword.ifEmpty { null }
        val selected = _contentUiState.value.selectedGenre
        getSearchContentList(keyword = keyword, genre = selected)
    }

    private fun getSearchContentList(keyword: String?, genre: String?) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _contentUiState.update { it.copy(searchResults = UiState.Loading) }

            val genreApiValue = genre?.let { OnboardingContentUiState.GENRES[it] }

            searchRepository.getSearchContentList(
                keyword = keyword,
                genre = genreApiValue,
            )
                .onSuccess { result ->
                    _contentUiState.update { currentState ->
                        currentState.copy(
                            searchResults = UiState.Success(result.contents)
                        )
                    }
                    Timber.d("Search result: $result")
                }
                .onFailure { error ->
                    _contentUiState.update { it.copy(searchResults = UiState.Failure) }
                    Timber.e(error)
                }
        }
    }

    fun toggleContentSelection(content: SearchContentItemModel) {
        _contentUiState.update { currentState ->
            val isAlreadySelected = currentState.selectedContents.any { it.id == content.id }

            val newSelectedContents = if (isAlreadySelected) {
                currentState.selectedContents.filterNot { it.id == content.id }
            } else {
                if (currentState.selectedContents.size < OnboardingContentUiState.REQUIRED_SELECTION_COUNT) {
                    listOf(content) + currentState.selectedContents
                } else {
                    currentState.selectedContents
                }
            }

            currentState.copy(selectedContents = newSelectedContents.toImmutableList())
        }
    }

    // ---------- onboarding ott ----------
    fun toggleOttSelection(ottType: OttType) {
        _ottUiState.update { currentState ->
            val isAlreadySelected = currentState.selectedOtts.contains(ottType)

            val newSelectedOtts = if (isAlreadySelected) {
                currentState.selectedOtts.filterNot { it == ottType }
            } else {
                currentState.selectedOtts + ottType
            }

            currentState.copy(selectedOtts = newSelectedOtts.toImmutableList())
        }
    }

    // ---------- onboarding signup ----------
    fun signup() {
        viewModelScope.launch {
            _signupUiState.update { it.copy(signupState = UiState.Loading) }

            val profileImageUrl = uploadProfileImageIfNeeded()

            val signupRequest = SignupRequestModel(
                tempToken = tempToken,
                nickname = _uiState.value.nickname,
                favoriteContentIds = _contentUiState.value.selectedContents.map { it.id },
                agreedTermsIds = _termsUiState.value.agreedTermsIds,
                profileImageUrl = profileImageUrl,
            )

            authRepository.signup(signupRequest)
                .onSuccess { response ->
                    _signupUiState.update { it.copy(signupState = UiState.Success(Unit)) }
                    Timber.d("Signup success: userId=${response.userId}")
                }
                .onFailure { error ->
                    _signupUiState.update { it.copy(signupState = UiState.Failure) }
                    Timber.e(error, "Signup failed")
                }
        }
    }

    private suspend fun uploadProfileImageIfNeeded(): String? {
        val uri = _uiState.value.profileImageUri ?: return null

        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val extension = mimeTypeToFileExtension(mimeType)

        val presignedUrl = storageRepository.getPresignedUrl(
            pathType = StoragePathType.USER_PROFILE,
            extension = extension,
        ).getOrElse { error ->
            Timber.e(error, "Failed to get presigned URL")
            return null
        }

        val imageBytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: return null

        storageRepository.uploadToS3(
            uploadUrl = presignedUrl.uploadUrl,
            imageBytes = imageBytes,
            mimeType = mimeType,
        ).getOrElse { error ->
            Timber.e(error, "Failed to upload profile image to S3")
            return null
        }

        return presignedUrl.key
    }

    private fun mimeTypeToFileExtension(mimeType: String): FileExtension = when (mimeType) {
        "image/png" -> FileExtension.PNG
        "image/gif" -> FileExtension.GIF
        "image/webp" -> FileExtension.WEBP
        else -> FileExtension.JPEG
    }
}