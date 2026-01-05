package com.flint.data.repositoryImpl

import com.flint.data.api.FlintApi
import com.flint.data.dto.request.SampleRequestDto
import com.flint.data.mapper.toModel
import com.flint.domain.model.SampleModel
import com.flint.domain.repository.FlintRepository

class DefaultFlintRepository(
    private val apiService: FlintApi
) : FlintRepository {
    override suspend fun sample(requestDto: SampleRequestDto): Result<SampleModel> = runCatching {
        apiService.sampleService(requestDto).data.toModel()
    }
}