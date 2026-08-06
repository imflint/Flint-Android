package com.flint.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.DataStoreKey.USER_NAME
import com.flint.core.common.util.UiState
import com.flint.data.local.PreferencesManager
import com.flint.domain.model.collection.CollectionListModel
import com.flint.domain.model.content.BookmarkedContentListModel
import com.flint.domain.model.ott.OttListModel
import com.flint.domain.model.ott.OttModel
import com.flint.domain.repository.HomeRepository
import com.flint.domain.repository.UserRepository
import com.flint.presentation.home.sideeffect.HomeSideEffect
import com.flint.presentation.home.uistate.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val homeRepository: HomeRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _userName = preferencesManager.getString(USER_NAME)
    private val _recommendCollectionListLoadState = MutableStateFlow<UiState<CollectionListModel>>(UiState.Loading)
    private val _bookmarkedContentListLoadState = MutableStateFlow<UiState<BookmarkedContentListModel>>(UiState.Loading)
    private val _popularCollectionListLoadState = MutableStateFlow<UiState<CollectionListModel>>(UiState.Loading)

    private val _homeSideEffect = MutableSharedFlow<HomeSideEffect>()
    val homeSideEffect = _homeSideEffect.asSharedFlow()

    val homeUiState: StateFlow<HomeUiState> = combine(
        _userName,
        _recommendCollectionListLoadState,
        _bookmarkedContentListLoadState,
        _popularCollectionListLoadState
    ) { userName, recommendedCollectionList, bookmarkedContentList, popularCollectionList ->
        HomeUiState(
            userName = userName,
            recommendedCollectionListLoadState = recommendedCollectionList,
            bookmarkedContentListLoadState = bookmarkedContentList,
            popularCollectionListLoadState = popularCollectionList
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = HomeUiState(
            userName = "",
            recommendedCollectionListLoadState = UiState.Loading,
            bookmarkedContentListLoadState = UiState.Loading,
            popularCollectionListLoadState = UiState.Loading
        )
    )

    fun getRecommendedCollectionList() = viewModelScope.launch {
        homeRepository.getRecommendedCollectionList()
            .onSuccess { _recommendCollectionListLoadState.emit(UiState.Success(it)) }
            .onFailure { Timber.e(it.message) }
    }

    fun getBookmarkedContentList() = viewModelScope.launch {
        userRepository.getUserBookmarkedContents(userId = null)
            .onSuccess { bookmarkedContents ->
                // 홈에서는 최근 저장한 콘텐츠 10개까지만 노출 (전체 목록은 프로필 > 저장한 콘텐츠에서 확인)
                _bookmarkedContentListLoadState.emit(
                    UiState.Success(
                        bookmarkedContents.copy(
                            contents = bookmarkedContents.contents
                                .take(MAX_SAVED_CONTENT_COUNT)
                                .toImmutableList(),
                        ),
                    ),
                )
            }
            .onFailure { Timber.e(it.message) }
    }

    fun getPopularCollectionList() = viewModelScope.launch {
        homeRepository.getPopularCollectionList()
            .onSuccess { _popularCollectionListLoadState.emit(UiState.Success(it)) }
            .onFailure { Timber.e(it.message) }
    }

    // 콘텐츠별 OTT 목록 API(/contents/ott/{id})가 빈 배열만 반환하므로
    // 이미 로드된 북마크 목록의 OTT 정보를 사용한다.
    // 프로필/저장한 콘텐츠 화면도 동일하게 getOttSimpleList를 쓴다.
    fun showOttList(contentId: String) = viewModelScope.launch {
        val otts = (_bookmarkedContentListLoadState.value as? UiState.Success)
            ?.data
            ?.contents
            ?.find { it.id == contentId }
            ?.getOttSimpleList
            .orEmpty()

        _homeSideEffect.emit(
            HomeSideEffect.ShowOttListBottomSheet(
                OttListModel(otts = otts.map { OttModel(name = it.name) }),
            ),
        )
    }

    companion object {
        private const val MAX_SAVED_CONTENT_COUNT = 10
    }
}