package com.flint.android.domain.repository

import com.flint.android.domain.type.FileExtension
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UploadImageReaderTest {

    @Test
    fun `서버가 받는 형식은 변환 없이 해당 확장자로 올린다`() {
        assertEquals(FileExtension.JPEG, uploadableExtensionOf("image/jpeg"))
        assertEquals(FileExtension.PNG, uploadableExtensionOf("image/png"))
        assertEquals(FileExtension.WEBP, uploadableExtensionOf("image/webp"))
    }

    @Test
    fun `HEIF와 HEIC는 변환 대상이다`() {
        assertNull(uploadableExtensionOf("image/heif"))
        assertNull(uploadableExtensionOf("image/heic"))
    }

    @Test
    fun `서버가 받지 않는 GIF와 형식을 모르는 경우도 변환 대상이다`() {
        assertNull(uploadableExtensionOf("image/gif"))
        assertNull(uploadableExtensionOf(null))
    }
}
