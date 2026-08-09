package com.flint.data.dto.ott

import com.flint.data.dto.base.BaseResponse
import com.flint.data.dto.ott.response.OttListResponseDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 콘텐츠별 OTT 목록 응답 역직렬화 테스트
 *
 * QA TC 3-33, 3-34: 홈에서 콘텐츠 카드를 눌러도 바텀시트가 뜨지 않는 문제
 *
 * GET /api/v1/contents/ott/{contentId} 의 서버 응답 스키마(GetOttResponse)는
 * { ottId, name, logoUrl } 세 필드뿐이고 contentUrl 은 존재하지 않는다.
 * DTO 가 contentUrl 을 기본값 없는 필수 필드로 선언하면 역직렬화가 실패하고,
 * 그 예외가 suspendRunCatching -> onFailure 로 흘러가 조용히 삼켜진다.
 */
class OttListResponseDtoTest {

    // NetworkModule 의 Json 설정과 동일하게 맞춘다
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
        prettyPrint = true
    }

    /** 스웨거 GetOttListRes 스키마 그대로 — contentUrl 없음 */
    private val serverResponse = """
        {
          "status": 200,
          "message": "OTT리스트 조회 성공",
          "data": {
            "otts": [
              { "ottId": "1", "name": "넷플릭스", "logoUrl": "https://cdn.flint/netflix.png" },
              { "ottId": "2", "name": "티빙", "logoUrl": "https://cdn.flint/tving.png" }
            ]
          }
        }
    """.trimIndent()

    @Test
    fun `contentUrl 이 없는 서버 응답을 역직렬화할 수 있다`() {
        val response = json.decodeFromString<BaseResponse<OttListResponseDto>>(serverResponse)

        assertEquals(2, response.data.otts.size)
        assertEquals("넷플릭스", response.data.otts[0].name)
        assertEquals("https://cdn.flint/tving.png", response.data.otts[1].logoUrl)
    }

    @Test
    fun `contentUrl 이 없으면 빈 문자열로 채운다`() {
        val response = json.decodeFromString<BaseResponse<OttListResponseDto>>(serverResponse)

        assertEquals("", response.data.otts[0].contentUrl)
    }

    @Test
    fun `서버가 contentUrl 을 내려주면 그 값을 사용한다`() {
        val withContentUrl = """
            {
              "otts": [
                {
                  "ottId": "1",
                  "name": "넷플릭스",
                  "logoUrl": "https://cdn.flint/netflix.png",
                  "contentUrl": "https://netflix.com/title/123"
                }
              ]
            }
        """.trimIndent()

        val dto = json.decodeFromString<OttListResponseDto>(withContentUrl)

        assertEquals("https://netflix.com/title/123", dto.otts[0].contentUrl)
    }

    @Test
    fun `볼 수 있는 OTT 가 없으면 빈 목록으로 역직렬화된다`() {
        val emptyResponse = """{ "otts": [] }"""

        val dto = json.decodeFromString<OttListResponseDto>(emptyResponse)

        assertEquals(0, dto.otts.size)
    }
}
