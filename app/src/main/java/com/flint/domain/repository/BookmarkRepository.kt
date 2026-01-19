package com.flint.domain.repository

import com.flint.data.api.BookmarkApi
import javax.inject.Inject

class BookmarkRepository
    @Inject
    constructor(
        private val apiService: BookmarkApi,
    ) {
        // 컬렉션 북마크 유저 조회

        // 컬렉션 북마크 토글

        // 콘텐츠 북마크 토글
    }
