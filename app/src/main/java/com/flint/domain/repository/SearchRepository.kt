package com.flint.domain.repository

import com.flint.data.api.SearchApi
import javax.inject.Inject

class SearchRepository
    @Inject
    constructor(
        private val apiService: SearchApi,
    ) {
        // 콘텐츠 검색
    }
