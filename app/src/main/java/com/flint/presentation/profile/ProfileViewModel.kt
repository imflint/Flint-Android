package com.flint.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.common.util.suspendRunCatching
import com.flint.core.navigation.MainTabRoute
import com.flint.domain.repository.ContentRepository
import com.flint.domain.repository.UserRepository
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
import com.flint.presentation.profile.sideeffect.ProfileSideEffect
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository
) : ViewModel() {

    val userId = savedStateHandle.toRoute<MainTabRoute.Profile>().userId

    private val _uiState = MutableStateFlow<UiState<ProfileUiState>>(
        UiState.Empty
    )
    val uiState: StateFlow<UiState<ProfileUiState>> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    fun getProfile() {
        viewModelScope.launch {
            suspendRunCatching {
                val profileDeferred = async { userRepository.getUserProfile(userId = userId).getOrThrow() }
                val keywordsDeferred = async { userRepository.getUserKeywords(userId = userId).getOrThrow() }

                ProfileUiState(
                    profile = profileDeferred.await(),
                    keywords = keywordsDeferred.await()
                )
            }.onSuccess { combinedState ->
                _uiState.update { UiState.Success(combinedState) }
                getSectionInfo()
            }
        }
    }

    fun getSectionInfo() {
        viewModelScope.launch {
            val currentState = (_uiState.value as? UiState.Success)?.data ?: return@launch

            suspendRunCatching {
                val createdCollectionsDeferred = async {
                    userRepository.getUserCreatedCollections(userId = userId).getOrThrow()
                }
                val bookmarkedCollectionsDeferred = async {
                    userRepository.getUserBookmarkedCollections(userId = userId).getOrThrow()
                }
                val savedContentListDeferred = async {
                    contentRepository.getBookmarkedContentListByUserId(userId = userId).getOrThrow()
                }

                currentState.copy(
                    createCollections = createdCollectionsDeferred.await(),
                    savedCollections = bookmarkedCollectionsDeferred.await(),
                    savedContents = savedContentListDeferred.await()
                )
            }.onSuccess { updatedState ->
                _uiState.update { UiState.Success(updatedState) }
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
}
