package com.flint.domain.repository

import com.flint.data.api.HomeApi
import javax.inject.Inject

class HomeRepository
    @Inject
    constructor(
        private val apiService: HomeApi,
    ) {
        // 추천 컬렉션 조회
    }
