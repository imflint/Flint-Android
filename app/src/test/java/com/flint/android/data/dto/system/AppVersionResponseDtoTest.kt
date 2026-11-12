package com.flint.android.data.dto.system

import com.flint.android.data.dto.base.BaseResponse
import com.flint.android.data.dto.system.response.AppVersionResponseDto
import com.flint.android.domain.mapper.system.toModel
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

/** 최소 지원 버전 응답 역직렬화 테스트 (스웨거 AppVersionRes 예시값) */
class AppVersionResponseDtoTest {

    // NetworkModule 의 Json 설정과 동일하게 맞춘다
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
        prettyPrint = true
    }

    @Test
    fun `최소 버전과 스토어 주소를 읽는다`() {
        val response = """
            {
              "status": 200,
              "message": "조회 성공",
              "data": {
                "version": "1.2.3",
                "path": "https://play.google.com/store/apps/details?id=com.flint.android"
              }
            }
        """.trimIndent()

        val model = json.decodeFromString<BaseResponse<AppVersionResponseDto>>(response).data.toModel()

        assertEquals("1.2.3", model.minimumVersion)
        assertEquals("https://play.google.com/store/apps/details?id=com.flint.android", model.storeUrl)
    }

    @Test
    fun `스토어 주소가 없으면 빈 문자열로 둔다`() {
        val response = """{ "status": 200, "message": "", "data": { "version": "1.0.0" } }"""

        val model = json.decodeFromString<BaseResponse<AppVersionResponseDto>>(response).data.toModel()

        assertEquals("", model.storeUrl)
    }
}
