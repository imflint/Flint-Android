package com.flint.domain.repository

import com.flint.data.dto.request.SampleRequestDto
import com.flint.domain.model.SampleModel

interface FlintRepository {
    suspend fun sample(requestDto: SampleRequestDto) : Result<SampleModel>
}