package com.flint.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.domain.repository.SearchRepository
import com.flint.domain.repository.UserRepository
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
import com.flint.domain.type.OttType

@HiltViewModel
class OnboardingViewModel
@Inject constructor(
    private val userRepository: UserRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingProfileUiState())
    val uiState: StateFlow<OnboardingProfileUiState> = _uiState.asStateFlow()

    private val _contentUiState = MutableStateFlow(OnboardingContentUiState())
    val contentUiState: StateFlow<OnboardingContentUiState> = _contentUiState.asStateFlow()

    // ---------- onboarding profile ----------
    fun updateNickname(nickname: String) {
        if (nickname.length <= OnboardingProfileUiState.MAX_LENGTH) {
            _uiState.update { currentState ->
                currentState.copy(
                    nickname = nickname,
                    isValid = nickname.length >= OnboardingProfileUiState.MIN_LENGTH,
                    isNicknameAvailable = null,
                )
            }
        }
    }

    fun checkNicknameDuplication() {
        val currentNickname = _uiState.value.nickname

        viewModelScope.launch {
            userRepository.checkNickname(currentNickname).onSuccess { result ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isNicknameAvailable = result.isAvailable,
                    )
                }
            }
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
                    currentState.selectedContents + content
                } else {
                    currentState.selectedContents
                }
            }

            currentState.copy(selectedContents = newSelectedContents.toImmutableList())
        }
    }

    // ---------- onboarding ott ----------
    private val _ottUiState = MutableStateFlow(OnboardingOttUiState())
    val ottUiState: StateFlow<OnboardingOttUiState> = _ottUiState.asStateFlow()

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

    // ---------- onboarding done ----------

}