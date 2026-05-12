package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.StorageApi
import com.flint.domain.mapper.storage.toModel
import com.flint.domain.model.storage.PresignedUrlModel
import com.flint.domain.type.FileExtension
import com.flint.domain.type.StoragePathType
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val api: StorageApi,
) {
    // Presigned URL 발급
    suspend fun getPresignedUrl(
        pathType: StoragePathType,
        extension: FileExtension,
    ): Result<PresignedUrlModel> =
        suspendRunCatching {
            api.getPresignedUrl(
                pathType = pathType.name,
                extension = extension.name,
            ).data.toModel()
        }
}
