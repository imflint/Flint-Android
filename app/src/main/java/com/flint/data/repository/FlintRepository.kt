package com.flint.data.repository

import com.flint.data.dto.SampleRequestDto
import com.flint.domain.model.SampleModel

interface FlintRepository {
    suspend fun sample(requestDto: SampleRequestDto): Result<SampleModel>
}
