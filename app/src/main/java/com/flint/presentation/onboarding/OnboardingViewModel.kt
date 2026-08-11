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
import com.flint.presentation.onboarding.event.OnboardingProfileEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    // 닉네임 검사 결과 토스트는 상태가 아니라 1회성 이벤트로 내려줘야
    // 화면 재진입(예: Done에서 뒤로가기) 시 토스트가 재발생하지 않는다.
    private val _profileEvent = MutableSharedFlow<OnboardingProfileEvent>()
    val profileEvent = _profileEvent.asSharedFlow()

    private var searchJob: Job? = null
    private var nicknameCheckJob: Job? = null

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

            // 형식 오류 토스트는 여기서 발행하지 않음 — "확인" 버튼을 눌렀을 때만 발행한다.
            // (타이핑 중 매 글자마다 토스트가 뜨는 문제 방지. 테두리 색은 isFormatValid로 계속 즉시 반영됨)
            _uiState.update { currentState ->
                currentState.copy(
                    nickname = nickname,
                    isValid = nickname.length >= OnboardingProfileUiState.MIN_LENGTH,
                    isFormatValid = isFormatValid,
                    isNicknameAvailable = null,
                    nicknameErrorType = if (!isFormatValid && nickname.isNotEmpty()) NicknameErrorType.INVALID_FORMAT else null,
                )
            }
        }
    }

    fun checkNicknameDuplication() {
        val currentNickname = _uiState.value.nickname

        // 형식이 유효하지 않으면(자모 단독 입력 포함) 서버 호출 없이 형식 오류 토스트만 노출
        if (!_uiState.value.isFormatValid) {
            viewModelScope.launch {
                _profileEvent.emit(
                    OnboardingProfileEvent.ShowNicknameToast(
                        message = "사용할 수 없는 닉네임입니다",
                        isSuccess = false,
                    )
                )
            }
            return
        }

        // 연타 시 이전 요청을 취소해 중복 요청과 토스트 중복 발행을 방지
        nicknameCheckJob?.cancel()
        nicknameCheckJob = viewModelScope.launch {
            userRepository.checkNickname(currentNickname)
                .onSuccess { result ->
                    // 응답이 오는 사이 닉네임이 바뀌었다면 이미 stale한 결과이므로 반영하지 않음
                    if (currentNickname != _uiState.value.nickname) return@onSuccess

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

                    // 버튼 클릭 시점에만 발행되는 1회성 이벤트라 화면 재진입 시엔 다시 뜨지 않음
                    _profileEvent.emit(
                        if (result.isAvailable) {
                            OnboardingProfileEvent.ShowNicknameToast(
                                message = "사용 가능한 닉네임입니다",
                                isSuccess = true,
                            )
                        } else {
                            OnboardingProfileEvent.ShowNicknameToast(
                                message = "이미 사용 중인 닉네임입니다",
                                isSuccess = false,
                            )
                        }
                    )
                }
                .onFailure { error ->
                     Timber.e(error, "닉네임 중복검사 실패했습니다.")
                }
        }
    }

    fun clearNicknameError() {
        _uiState.update { currentState ->
            currentState.copy(nicknameErrorType = null)
        }
    }

    // 닉네임 입력 페이지 재진입 시(예: End 화면에서 뒤로가기) 호출.
    // 기존에 입력한 닉네임 텍스트는 유지하되, 중복확인 결과는 초기화해
    // 사용자가 "다음"으로 넘어가기 전 중복확인을 다시 하도록 강제한다.
    fun resetNicknameCheck() {
        nicknameCheckJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                isNicknameAvailable = null,
                nicknameErrorType = null,
            )
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
        getSearchContentList(keyword = null, genres = emptySet())
    }

    fun clearSearchKeyword() {
        _contentUiState.update { it.copy(searchKeyword = "") }
        val genres = _contentUiState.value.selectedGenres
        getSearchContentList(keyword = null, genres = genres)
    }

    fun searchContents() {
        val keyword = _contentUiState.value.searchKeyword.ifEmpty { null }
        val genres = _contentUiState.value.selectedGenres
        getSearchContentList(keyword = keyword, genres = genres)
    }

    fun selectGenre(genre: String) {
        _contentUiState.update { currentState ->
            val newSelected = if (currentState.selectedGenres.contains(genre)) {
                currentState.selectedGenres - genre
            } else {
                currentState.selectedGenres + genre
            }
            currentState.copy(selectedGenres = newSelected)
        }
        // 장르 선택/해제 시 즉시 재검색
        val keyword = _contentUiState.value.searchKeyword.ifEmpty { null }
        val selected = _contentUiState.value.selectedGenres
        getSearchContentList(keyword = keyword, genres = selected)
    }

    private fun getSearchContentList(keyword: String?, genres: Set<String>) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _contentUiState.update { it.copy(searchResults = UiState.Loading, nextCursor = null) }

            val genreApiValues = genres.mapNotNull { OnboardingContentUiState.GENRES[it] }
                .ifEmpty { null }

            searchRepository.getSearchContentList(
                keyword = keyword,
                genres = genreApiValues,
                cursor = null,
            )
                .onSuccess { result ->
                    _contentUiState.update { currentState ->
                        currentState.copy(
                            searchResults = UiState.Success(result.contents),
                            nextCursor = result.nextCursor,
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

    fun loadMoreContents() {
        val state = _contentUiState.value
        val cursor = state.nextCursor ?: return
        if (state.isLoadingMore) return
        val currentItems = (state.searchResults as? UiState.Success)?.data ?: return

        viewModelScope.launch {
            _contentUiState.update { it.copy(isLoadingMore = true) }

            val keyword = state.searchKeyword.ifEmpty { null }
            val genreApiValues = state.selectedGenres
                .mapNotNull { OnboardingContentUiState.GENRES[it] }
                .ifEmpty { null }

            searchRepository.getSearchContentList(
                keyword = keyword,
                genres = genreApiValues,
                cursor = cursor,
            )
                .onSuccess { result ->
                    val merged = (currentItems + result.contents).toImmutableList()
                    _contentUiState.update { it.copy(
                        searchResults = UiState.Success(merged),
                        nextCursor = result.nextCursor,
                        isLoadingMore = false,
                    )}
                }
                .onFailure { error ->
                    _contentUiState.update { it.copy(isLoadingMore = false) }
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

        val mimeType = withContext(Dispatchers.IO) {
            context.contentResolver.getType(uri)
        } ?: "image/jpeg"
        val extension = mimeTypeToFileExtension(mimeType)

        val presignedUrl = storageRepository.getPresignedUrl(
            pathType = StoragePathType.USER_PROFILE,
            extension = extension,
        ).getOrElse { error ->
            Timber.e(error, "Failed to get presigned URL")
            return null
        }

        val imageBytes = withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        } ?: return null

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
