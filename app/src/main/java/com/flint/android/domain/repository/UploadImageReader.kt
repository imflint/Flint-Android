package com.flint.android.domain.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.flint.android.core.common.util.suspendRunCatching
import com.flint.android.domain.type.FileExtension
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

data class UploadImage(
    val bytes: ByteArray,
    val mimeType: String,
    val extension: FileExtension,
)

// 서버의 이미지 업로드 경로(프로필·컬렉션)는 JPG/JPEG/PNG/WEBP 만 허용한다.
// 갤러리에서 고른 이미지가 HEIF(HEIC) 등 그 외 형식이면 디코딩해서 JPEG(투명 영역이 있으면 PNG)로 바꿔 올린다.
class UploadImageReader @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    suspend fun read(uri: Uri): Result<UploadImage> =
        suspendRunCatching {
            withContext(Dispatchers.IO) {
                val mimeType = context.contentResolver.getType(uri)
                val extension = uploadableExtensionOf(mimeType)
                if (mimeType != null && extension != null) {
                    val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                        ?: error("Failed to open image stream: $uri")
                    UploadImage(bytes = bytes, mimeType = mimeType, extension = extension)
                } else {
                    reencode(uri)
                }
            }
        }

    private fun reencode(uri: Uri): UploadImage {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        // 회전 정보는 디코딩 시 반영되므로 변환 결과는 바로 선 이미지가 된다.
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
        return try {
            val output = ByteArrayOutputStream()
            if (bitmap.hasAlpha()) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
                UploadImage(bytes = output.toByteArray(), mimeType = "image/png", extension = FileExtension.PNG)
            } else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, output)
                UploadImage(bytes = output.toByteArray(), mimeType = "image/jpeg", extension = FileExtension.JPEG)
            }
        } finally {
            bitmap.recycle()
        }
    }

    private companion object {
        const val JPEG_QUALITY = 90
    }
}

/** 변환 없이 그대로 올릴 수 있는 형식이면 서버에 넘길 확장자, 아니면 null */
internal fun uploadableExtensionOf(mimeType: String?): FileExtension? =
    when (mimeType) {
        "image/jpeg" -> FileExtension.JPEG
        "image/png" -> FileExtension.PNG
        "image/webp" -> FileExtension.WEBP
        else -> null
    }
