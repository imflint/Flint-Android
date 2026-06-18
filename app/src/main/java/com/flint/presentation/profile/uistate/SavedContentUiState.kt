package com.flint.presentation.profile.uistate

import androidx.compose.runtime.Immutable
import com.flint.core.common.util.UiState
import com.flint.domain.model.content.BookmarkedContentItemModel
import com.flint.domain.model.content.BookmarkedContentListModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Immutable
data class SavedContentUiState(
    val searchKeyword: String = "",
    val contents: UiState<BookmarkedContentListModel> = UiState.Loading,
    val showBookmarkRestrictionModal: Boolean = false,
) {
    /**
     * 검색어로 필터링된 콘텐츠 목록
     * 검색어가 비어있으면 전체 목록을 반환
     */
    val filteredContents: ImmutableList<BookmarkedContentItemModel>
        get() {
            val all = (contents as? UiState.Success)?.data?.contents ?: persistentListOf()
            return if (searchKeyword.isBlank()) {
                all
            } else {
                all.filter { it.title.contains(searchKeyword.trim(), ignoreCase = true) }
                    .toPersistentList()
            }
        }

    /**
     * 화면 상단 "총 n개"에 사용할 카운트 (전체 저장 작품 수).
     */
    val totalCount: Int
        get() = (contents as? UiState.Success)?.data?.totalCount ?: 0

    companion object {
        /**
         * 취향 키워드 분석을 위해 최소 유지해야 하는 저장 작품 수.
         * 저장 작품이 이 값과 같을 때 북마크 취소를 시도하면 안내 모달을 노출한다.
         */
        const val MIN_REQUIRED_COUNT = 5

        val Empty = SavedContentUiState()

        val Fake = SavedContentUiState(
            contents = UiState.Success(BookmarkedContentListModel.FakeList),
        )
    }
}
