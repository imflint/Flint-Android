package com.flint.android.domain.repository

import android.net.Uri
import com.flint.android.domain.type.StoragePathType
import javax.inject.Inject

// 온보딩(회원가입)과 프로필 수정 화면에서 공통으로 쓰는
// "이미지 읽기(필요하면 형식 변환) -> presigned URL 발급 -> S3 업로드 -> 서버에 반영" 흐름을 한 곳에 모아둔다.
class ProfileImageUploader @Inject constructor(
    private val uploadImageReader: UploadImageReader,
    private val storageRepository: StorageRepository,
    private val userRepository: UserRepository,
) {
    suspend fun upload(uri: Uri): Result<Unit> {
        val image = uploadImageReader.read(uri).getOrElse { error -> return Result.failure(error) }

        val presignedUrl = storageRepository.getPresignedUrl(
            pathType = StoragePathType.USER_PROFILE,
            extension = image.extension,
        ).getOrElse { error -> return Result.failure(error) }

        storageRepository.uploadToS3(
            uploadUrl = presignedUrl.uploadUrl,
            imageBytes = image.bytes,
            mimeType = image.mimeType,
        ).getOrElse { error -> return Result.failure(error) }

        return userRepository.updateProfileImage(presignedUrl.key)
    }
}
