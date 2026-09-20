package com.flint.android.data.dto.home

import com.flint.android.data.dto.home.response.PopularCollectionResponseDto
import com.flint.android.domain.mapper.collection.toModel
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 인기 컬렉션 응답 역직렬화 테스트
 *
 * QA TC 3-1: 인기 컬렉션 리스트에서 저장 상태/저장 수가 반영되지 않는 문제
 *
 * GET /api/v1/home/popular-collections 는 bookmarkCount 와 isBookmarked 를 내려주지만
 * DTO 가 두 필드를 선언하지 않으면 ignoreUnknownKeys = true 때문에 조용히 버려지고,
 * CollectionItemModel 의 기본값(false, 0)으로 떨어진다.
 */
class PopularCollectionResponseDtoTest {

    // NetworkModule 의 Json 설정과 동일하게 맞춘다
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
        prettyPrint = true
    }

    /** 스웨거 getPopularCollections 성공 예시 그대로 */
    private val serverResponse = """
        {
          "collections": [
            {
              "id": "800388257884431200",
              "thumbnailUrl": "https://cdn.flint.kr/collection/cover/800388.jpg",
              "title": "주말에 보기 좋은 한국 영화",
              "imageList": [
                "https://cdn.flint.kr/content/poster/100.jpg",
                "https://cdn.flint.kr/content/poster/101.jpg"
              ],
              "bookmarkCount": 15,
              "isBookmarked": true,
              "nickname": "플린트",
              "profileImageUrl": "https://cdn.flint.kr/user/profile/123.jpg"
            },
            {
              "id": "800388257884431201",
              "thumbnailUrl": "https://cdn.flint.kr/collection/cover/800389.jpg",
              "title": "비 오는 날 듣기 좋은 OST 영화",
              "imageList": [
                "https://cdn.flint.kr/content/poster/102.jpg",
                "https://cdn.flint.kr/content/poster/103.jpg"
              ],
              "bookmarkCount": 12,
              "isBookmarked": false,
              "nickname": "수채한",
              "profileImageUrl": "https://cdn.flint.kr/user/profile/124.jpg"
            }
          ]
        }
    """.trimIndent()

    @Test
    fun `서버가 내려준 저장 여부를 그대로 읽는다`() {
        val dto = json.decodeFromString<PopularCollectionResponseDto>(serverResponse)

        assertTrue(dto.collections[0].isBookmarked)
        assertFalse(dto.collections[1].isBookmarked)
    }

    @Test
    fun `서버가 내려준 저장 수를 그대로 읽는다`() {
        val dto = json.decodeFromString<PopularCollectionResponseDto>(serverResponse)

        assertEquals(15, dto.collections[0].bookmarkCount)
        assertEquals(12, dto.collections[1].bookmarkCount)
    }

    @Test
    fun `저장 여부와 저장 수가 모델까지 전달된다`() {
        val model = json.decodeFromString<PopularCollectionResponseDto>(serverResponse).toModel()

        assertEquals(2, model.collections.size)
        assertTrue(model.collections[0].isBookmarked)
        assertEquals(15, model.collections[0].bookmarkCount)
        assertFalse(model.collections[1].isBookmarked)
        assertEquals(12, model.collections[1].bookmarkCount)
    }

    @Test
    fun `토큰 없이 조회해 두 필드가 빠져도 역직렬화는 성공한다`() {
        val withoutBookmarkFields = """
            {
              "collections": [
                {
                  "id": "800388257884431200",
                  "thumbnailUrl": null,
                  "title": "주말에 보기 좋은 한국 영화",
                  "imageList": [],
                  "nickname": "플린트",
                  "profileImageUrl": null
                }
              ]
            }
        """.trimIndent()

        val model = json.decodeFromString<PopularCollectionResponseDto>(withoutBookmarkFields).toModel()

        assertFalse(model.collections[0].isBookmarked)
        assertEquals(0, model.collections[0].bookmarkCount)
    }
}
