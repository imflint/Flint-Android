package com.flint.android.domain.repository

import android.content.Context
import android.net.Uri
import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.domain.type.FileExtension
import com.flint.android.domain.type.StoragePathType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// 온보딩(회원가입)과 프로필 수정 화면에서 공통으로 쓰는
// "이미지 읽기 -> presigned URL 발급 -> S3 업로드 -> 서버에 반영" 흐름을 한 곳에 모아둔다.
class ProfileImageUploader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository,
) {
    suspend fun upload(uri: Uri): Result<Unit> {
        val (mimeType, imageBytes) = suspendRunCatching {
            withContext(Dispatchers.IO) {
                val resolvedMimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
                val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                resolvedMimeType to bytes
            }
        }.getOrElse { error -> return Result.failure(error) }

        if (imageBytes == null) {
            return Result.failure(IllegalStateException("Failed to open image stream"))
        }

        val extension = mimeTypeToFileExtension(mimeType)

        val presignedUrl = storageRepository.getPresignedUrl(
            pathType = StoragePathType.USER_PROFILE,
            extension = extension,
        ).getOrElse { error -> return Result.failure(error) }

        storageRepository.uploadToS3(
            uploadUrl = presignedUrl.uploadUrl,
            imageBytes = imageBytes,
            mimeType = mimeType,
        ).getOrElse { error -> return Result.failure(error) }

        return userRepository.updateProfileImage(presignedUrl.key)
    }

    private fun mimeTypeToFileExtension(mimeType: String): FileExtension = when (mimeType) {
        "image/png" -> FileExtension.PNG
        "image/gif" -> FileExtension.GIF
        "image/webp" -> FileExtension.WEBP
        else -> FileExtension.JPEG
    }
}
