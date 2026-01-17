package com.flint.data.repositoryImpl

import com.flint.data.api.FlintApi
import com.flint.data.dto.SampleRequestDto
import com.flint.data.mapper.toModel
import com.flint.domain.model.SampleModel
import com.flint.data.repository.FlintRepository
import javax.inject.Inject

class DefaultFlintRepository
    @Inject
    constructor(
        private val apiService: FlintApi,
    ) : FlintRepository {
        override suspend fun sample(requestDto: SampleRequestDto): Result<SampleModel> =
            runCatching {
                apiService.sampleService(requestDto).data.toModel()
            }
    }
