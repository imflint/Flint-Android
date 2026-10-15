package com.flint.android.data.dto.exploration

import com.flint.android.data.dto.exploration.response.ExplorationResponseDto
import com.flint.android.domain.mapper.exploration.toModel
import com.flint.android.domain.model.exploration.ExplorationState
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 탐색 세션 응답 역직렬화 테스트
 *
 * 이어보기는 lastViewedPosition 과 item.position 에 기대는데,
 * DTO 가 필드를 빠뜨리면 ignoreUnknownKeys = true 때문에 조용히 버려져 항상 처음부터 보이게 된다.
 */
class ExplorationResponseDtoTest {

    // NetworkModule 의 Json 설정과 동일하게 맞춘다
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
        prettyPrint = true
    }

    /** 스웨거 ExplorationSessionRes 스키마 예시값으로 구성 (2번 위치 작품은 숨김 처리돼 빠진 상황) */
    private val serverResponse = """
        {
          "items": [
            {
              "contentId": "801473411402740986",
              "title": "인터스텔라",
              "description": "우주 저편의 감정을 섬세하게 풀어낸 작품이에요.",
              "imageUrl": "https://cdn.flint.kr/content/poster/100.jpg",
              "year": 2014,
              "collectionId": "801473411402741000",
              "position": 1
            },
            {
              "contentId": "801473411402740987",
              "title": "그녀",
              "description": "",
              "imageUrl": "https://cdn.flint.kr/content/poster/101.jpg",
              "year": 2013,
              "collectionId": "801473411402741001",
              "position": 3
            }
          ],
          "state": "IN_PROGRESS",
          "hasNext": false,
          "lastViewedPosition": 12,
          "canAdvance": false
        }
    """.trimIndent()

    @Test
    fun `진행 위치와 작품 위치가 모델까지 그대로 전달된다`() {
        val model = json.decodeFromString<ExplorationResponseDto>(serverResponse).toModel()

        assertEquals(ExplorationState.IN_PROGRESS, model.state)
        assertEquals(12, model.lastViewedPosition)
        assertEquals(listOf(1, 3), model.items.map { it.position })
    }

    @Test
    fun `END 응답이 END 상태로 전달된다`() {
        val endResponse = serverResponse.replace("\"IN_PROGRESS\"", "\"END\"")

        val model = json.decodeFromString<ExplorationResponseDto>(endResponse).toModel()

        assertTrue(model.isEnd)
    }

    @Test
    fun `next 엔드포인트용 필드가 응답에서 빠져도 파싱된다`() {
        val withoutNextFields = serverResponse
            .replace(Regex("\\s*\"hasNext\": false,"), "")
            .replace(Regex(",\\s*\"canAdvance\": false"), "")

        val model = json.decodeFromString<ExplorationResponseDto>(withoutNextFields).toModel()

        assertEquals(12, model.lastViewedPosition)
    }
}
