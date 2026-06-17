package com.flint.domain.model.bookmark

sealed class BookmarkException : Exception() {
    /** 최소 저장 작품 수 제한으로 콘텐츠 북마크 해제 불가 */
    object ContentMinLimitExceeded : BookmarkException()
}
