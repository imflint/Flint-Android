package com.flint.presentation.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.navigation.Route
import com.flint.domain.model.auth.SignupRequestModel
import com.flint.domain.repository.AuthRepository
import com.flint.domain.repository.SearchRepository
import com.flint.domain.repository.UserRepository
import com.flint.domain.type.OttType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import com.flint.domain.model.search.SearchContentItemModel

@HiltViewModel
class OnboardingViewModel
@Inject constructor(
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val tempToken: String = savedStateHandle.toRoute<Route.OnboardingGraph>().tempToken

    private val _uiState = MutableStateFlow(OnboardingProfileUiState())
    val uiState: StateFlow<OnboardingProfileUiState> = _uiState.asStateFlow()

    private val _contentUiState = MutableStateFlow(OnboardingContentUiState())
    val contentUiState: StateFlow<OnboardingContentUiState> = _contentUiState.asStateFlow()

    private val _ottUiState = MutableStateFlow(OnboardingOttUiState())
    val ottUiState: StateFlow<OnboardingOttUiState> = _ottUiState.asStateFlow()

    private val _signupUiState = MutableStateFlow(OnboardingSignupUiState())
    val signupUiState: StateFlow<OnboardingSignupUiState> = _signupUiState.asStateFlow()

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

    // ---------- onboarding content ----------
    fun updateSearchKeyword(keyword: String) {
        _contentUiState.update { currentState ->
            currentState.copy(searchKeyword = keyword)
        }
    }

    fun loadInitialContents() {
        getSearchContentList(null)
    }

    fun searchContents() {
        val keyword = _contentUiState.value.searchKeyword
        getSearchContentList(keyword.ifEmpty { null })
    }

    private fun getSearchContentList(keyword: String?) {
        viewModelScope.launch {
            _contentUiState.update { it.copy(searchResults = UiState.Loading) }

            searchRepository.getSearchContentList(keyword)
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

            val signupRequest = SignupRequestModel(
                tempToken = tempToken,
                nickname = _uiState.value.nickname,
                favoriteContentIds = _contentUiState.value.selectedContents.map { it.id.toLong() },
                subscribedOttIds = _ottUiState.value.selectedOtts.map { it.id }
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
}