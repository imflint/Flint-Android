package com.flint.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.common.util.UiState
import com.flint.core.common.util.suspendRunCatching
import com.flint.core.navigation.Route
import com.flint.domain.model.bookmark.BookmarkChange
import com.flint.domain.model.user.KeywordListModel
import com.flint.domain.repository.AuthRepository
import com.flint.domain.repository.BookmarkRepository
import com.flint.domain.repository.ContentRepository
import com.flint.domain.repository.UserRepository
import com.flint.presentation.profile.sideeffect.ProfileSideEffect
import com.flint.presentation.profile.uistate.ProfileSectionData
import com.flint.presentation.profile.uistate.ProfileUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
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
    private val contentRepository: ContentRepository,
    private val bookmarkRepository: BookmarkRepository,
) : ViewModel() {

    val userId = savedStateHandle.toRoute<Route.Profile>().userId

    private val _uiState = MutableStateFlow(ProfileUiState(userId = userId))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        getProfile()
        observeBookmarkChanges()
    }

    private fun observeBookmarkChanges() {
        viewModelScope.launch {
            bookmarkRepository.bookmarkChanges.collect { change ->
                _uiState.update { state ->
                    val data = (state.sectionData as? UiState.Success)?.data ?: return@update state
                    val updated = when (change) {
                        is BookmarkChange.Content -> {
                            val updatedContents = if (userId == null) {
                                if (change.isBookmarked) {
                                    data.savedContents
                                } else {
                                    val filtered = data.savedContents.contents
                                        .filter { it.id != change.id }
                                        .toPersistentList()
                                    data.savedContents.copy(
                                        contents = filtered,
                                        totalCount = maxOf(data.savedContents.totalCount - 1, 0),
                                    )
                                }
                            } else {
                                data.savedContents.copy(
                                    contents = data.savedContents.contents
                                        .map {
                                            if (it.id == change.id) {
                                                it.copy(isBookmarked = change.isBookmarked)
                                            } else {
                                                it
                                            }
                                        }
                                        .toPersistentList(),
                                )
                            }
                            data.copy(savedContents = updatedContents)
                        }
                        is BookmarkChange.Collection -> {
                            val updatedCollections = if (change.isBookmarked) {
                                data.savedCollections
                            } else {
                                val filtered = data.savedCollections.collections
                                    .filter { it.id != change.id }
                                    .toPersistentList()
                                data.savedCollections.copy(collections = filtered)
                            }
                            data.copy(savedCollections = updatedCollections)
                        }
                    }
                    state.copy(sectionData = UiState.Success(updated))
                }
            }
        }
    }

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

    fun recalculateKeywords() = viewModelScope.launch {
        _uiState.update { it.copy(isRecalculating = true) }
        userRepository.recalculateKeywords()
            .onSuccess {
                // 버튼 즉시 비활성화 후 키워드 재조회
                _uiState.update { it.copy(profile = it.profile.copy(keywordRecalculatable = false)) }
                userRepository.getUserKeywords(userId = null)
                    .onSuccess { keywords ->
                        _uiState.update { state ->
                            val current = (state.sectionData as? UiState.Success)?.data ?: return@update state
                            state.copy(sectionData = UiState.Success(current.copy(keywords = keywords)))
                        }
                    }
                    .onFailure { Timber.e(it) }
            }
            .onFailure { Timber.e(it) }
        _uiState.update { it.copy(isRecalculating = false) }
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
