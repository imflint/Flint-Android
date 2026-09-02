package com.flint.android.presentation.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.android.core.analytics.AnalyticsTracker
import com.flint.android.core.analytics.FlintEvent
import com.flint.android.core.common.util.UiState
import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.core.navigation.Route
import com.flint.android.domain.model.bookmark.BookmarkChange
import com.flint.android.domain.model.user.KeywordListModel
import com.flint.android.domain.repository.BookmarkRepository
import com.flint.android.domain.repository.CollectionRepository
import com.flint.android.domain.repository.ContentRepository
import com.flint.android.domain.repository.UserRepository
import com.flint.android.presentation.profile.sideeffect.ProfileSideEffect
import com.flint.android.presentation.profile.uistate.ProfileSectionData
import com.flint.android.presentation.profile.uistate.ProfileUiState
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
    private val userRepository: UserRepository,
    private val contentRepository: ContentRepository,
    private val bookmarkRepository: BookmarkRepository,
    private val collectionRepository: CollectionRepository,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    val userId = savedStateHandle.toRoute<Route.Profile>().userId

    private val _uiState = MutableStateFlow(ProfileUiState(userId = userId))
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ProfileSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        getProfile()
        observeBookmarkChanges()
        observeCollectionDeletions()
    }

    // 컬렉션 상세 등 다른 화면에서 컬렉션을 삭제해도
    // 내가 생성한 컬렉션 / 저장한 컬렉션 가로 리스트에 즉시 반영되도록 구독한다.
    private fun observeCollectionDeletions() {
        viewModelScope.launch {
            collectionRepository.collectionDeletions.collect { deletedCollectionId ->
                _uiState.update { state ->
                    val data = (state.sectionData as? UiState.Success)?.data ?: return@update state
                    val updated = data.copy(
                        createCollections = data.createCollections.copy(
                            collections = data.createCollections.collections
                                .filter { it.id != deletedCollectionId }
                                .toPersistentList(),
                        ),
                        savedCollections = data.savedCollections.copy(
                            collections = data.savedCollections.collections
                                .filter { it.id != deletedCollectionId }
                                .toPersistentList(),
                        ),
                    )
                    state.copy(sectionData = UiState.Success(updated))
                }
            }
        }
    }

    private fun observeBookmarkChanges() {
        viewModelScope.launch {
            bookmarkRepository.bookmarkChanges.collect { change ->
                when (change) {
                    is BookmarkChange.Content -> handleContentBookmarkChange(change)
                    is BookmarkChange.Collection -> handleCollectionBookmarkChange(change)
                }
            }
        }
    }

    private fun handleContentBookmarkChange(change: BookmarkChange.Content) {
        if (userId == null && change.isBookmarked) {
            // 내 프로필에서 재북마크된 경우: 취소되면서 목록에서 이미 제거된 아이템은
            // 로컬에 제목/이미지 등 정보가 남아있지 않아 그대로 되살릴 수 없다.
            // 그래서 depth 0 가로 리스트에 정확한 데이터/순서로 다시 보이도록 목록 자체를 다시 불러온다.
            // (다른 화면의 북마크 토글이 이 네트워크 호출 때문에 밀리지 않도록 별도 코루틴으로 처리한다.)
            viewModelScope.launch {
                userRepository.getUserBookmarkedContents(userId = null)
                    .onSuccess { refreshed ->
                        _uiState.update { state ->
                            val current = (state.sectionData as? UiState.Success)?.data ?: return@update state
                            state.copy(sectionData = UiState.Success(current.copy(savedContents = refreshed)))
                        }
                    }
                    .onFailure { Timber.e(it, "Failed to reload saved contents after re-bookmark") }
            }
            return
        }

        _uiState.update { state ->
            val data = (state.sectionData as? UiState.Success)?.data ?: return@update state
            val updatedContents = if (userId == null) {
                // 내 프로필: 북마크 취소 시 목록에서 제거
                data.savedContents.copy(
                    totalCount = maxOf(0, data.savedContents.totalCount - 1),
                    contents = data.savedContents.contents
                        .filter { it.id != change.id }
                        .toPersistentList(),
                )
            } else {
                // 타 유저 프로필: isBookmarked 토글만 (상대방 목록에서 제거 X)
                data.savedContents.copy(
                    contents = data.savedContents.contents
                        .map { if (it.id == change.id) it.copy(isBookmarked = change.isBookmarked) else it }
                        .toPersistentList()
                )
            }
            state.copy(sectionData = UiState.Success(data.copy(savedContents = updatedContents)))
        }
    }

    private fun handleCollectionBookmarkChange(change: BookmarkChange.Collection) {
        if (userId == null && change.isBookmarked) {
            // 콘텐츠와 동일한 이유로, 내 프로필에서 재북마크된 컬렉션도 목록을 다시 불러온다.
            viewModelScope.launch {
                userRepository.getUserBookmarkedCollections(userId = null)
                    .onSuccess { refreshed ->
                        _uiState.update { state ->
                            val current = (state.sectionData as? UiState.Success)?.data ?: return@update state
                            state.copy(sectionData = UiState.Success(current.copy(savedCollections = refreshed)))
                        }
                    }
                    .onFailure { Timber.e(it, "Failed to reload saved collections after re-bookmark") }
            }
            return
        }

        _uiState.update { state ->
            val data = (state.sectionData as? UiState.Success)?.data ?: return@update state
            val updatedCollections = if (userId == null) {
                // 내 프로필: 북마크 취소 시 목록에서 제거
                val filtered = data.savedCollections.collections
                    .filter { it.id != change.id }
                    .toPersistentList()
                data.savedCollections.copy(collections = filtered)
            } else {
                // 타 유저 프로필: isBookmarked 토글만 (상대방 목록에서 제거 X)
                data.savedCollections.copy(
                    collections = data.savedCollections.collections
                        .map { if (it.id == change.id) it.copy(isBookmarked = change.isBookmarked) else it }
                        .toPersistentList()
                )
            }
            state.copy(sectionData = UiState.Success(data.copy(savedCollections = updatedCollections)))
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

    // 프로필 헤더만 재조회 (섹션 데이터는 유지)
    fun reloadUserProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile(userId = userId)
                .onSuccess { profile ->
                    _uiState.update { it.copy(profile = profile) }
                }
                .onFailure { Timber.e(it) }
        }
    }

    fun recalculateKeywords() = viewModelScope.launch {
        _uiState.update { it.copy(isRecalculating = true) }
        userRepository.recalculateKeywords()
            .onSuccess {
                // 정의서의 update_keyword. 재계산이 성공한 시점만 집계한다.
                analyticsTracker.track(FlintEvent.UpdateKeyword)

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
}
