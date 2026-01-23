package com.flint.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.common.util.suspendRunCatching
import com.flint.core.navigation.Route
import com.flint.domain.model.user.KeywordListModel
import com.flint.domain.repository.AuthRepository
import com.flint.domain.repository.ContentRepository
import com.flint.domain.repository.UserRepository
import com.flint.presentation.profile.sideeffect.ProfileSideEffect
import com.flint.presentation.profile.uistate.ProfileSectionData
import com.flint.presentation.profile.uistate.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {

    val userId = savedStateHandle.toRoute<Route.Profile>().userId

    private val _uiState = MutableStateFlow(ProfileUiState(userId = userId))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun getProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile(userId = userId)
                .onSuccess { profile ->
                    _uiState.update { it.copy(profile = profile) }
                    loadSectionData()
                }
                .onFailure {
                    Timber.e(it)
                }
        }
    }

    private fun loadSectionData() {
        viewModelScope.launch {
            _uiState.update { it.copy(sectionData = UiState.Loading) }

            suspendRunCatching {
                val keywordsDeferred = async {
                    userRepository.getUserKeywords(userId = userId).getOrDefault(KeywordListModel())
                }
                val createdCollectionsDeferred = async {
                    userRepository.getUserCreatedCollections(userId = userId).getOrThrow()
                }
                val bookmarkedCollectionsDeferred = async {
                    userRepository.getUserBookmarkedCollections(userId = userId).getOrThrow()
                }
                val savedContentListDeferred = async {
                    userRepository.getUserBookmarkedContents(userId = userId).getOrThrow()
                }

                ProfileSectionData(
                    keywords = keywordsDeferred.await(),
                    createCollections = createdCollectionsDeferred.await(),
                    savedCollections = bookmarkedCollectionsDeferred.await(),
                    savedContents = savedContentListDeferred.await()
                )
            }.onSuccess { sectionData ->
                _uiState.update { it.copy(sectionData = UiState.Success(sectionData)) }
            }.onFailure {
                _uiState.update { it.copy(sectionData = UiState.Failure) }
                Timber.e(it)
            }
        }
    }

    fun getOttListPerContent(contentId: String) = viewModelScope.launch {
        contentRepository.getOttListPerContent(contentId)
            .onSuccess {
                _sideEffect.emit(ProfileSideEffect.ShowOttListBottomSheet(it))
            }
            .onFailure {
                Timber.e(it)
            }
    }

    fun easterEggWithdraw() = viewModelScope.launch {
        authRepository.withdraw()
            .onSuccess {
                _sideEffect.emit(ProfileSideEffect.WithdrawSuccess)
            }
            .onFailure {
                Timber.e(it)
            }
    }
}
