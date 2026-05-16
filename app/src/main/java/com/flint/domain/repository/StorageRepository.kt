package com.flint.domain.repository

import com.flint.core.common.util.suspendRunCatching
import com.flint.data.api.StorageApi
import com.flint.data.di.qualifier.S3OkHttpClient
import com.flint.domain.mapper.storage.toModel
import com.flint.domain.model.storage.PresignedUrlModel
import com.flint.domain.type.FileExtension
import com.flint.domain.type.StoragePathType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val api: StorageApi,
    @S3OkHttpClient private val s3Client: OkHttpClient,
) {
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

    suspend fun uploadToS3(
        uploadUrl: String,
        imageBytes: ByteArray,
        mimeType: String,
    ): Result<Unit> = suspendRunCatching {
        withContext(Dispatchers.IO) {
            val requestBody = imageBytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val request = Request.Builder()
                .url(uploadUrl)
                .put(requestBody)
                .build()
            s3Client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) error("S3 upload failed: ${response.code}")
            }
        }
    }
}
