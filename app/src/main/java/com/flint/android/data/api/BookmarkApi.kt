package com.flint.android.data.api

import com.flint.android.data.dto.base.BaseResponse
import com.flint.android.data.dto.bookmark.CollectionBookmarkUsersDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BookmarkApi {
    // 컬렉션 북마크 유저 조회
    @GET("/api/v1/bookmarks/{collectionId}")
    suspend fun getCollectionBookmarkUsers(
        @Path("collectionId") collectionId: String,
    ): BaseResponse<CollectionBookmarkUsersDto>

    // 컬렉션 북마크 토글
    @POST("/api/v1/bookmarks/collections/{collectionId}")
    suspend fun toggleCollectionBookmark(
        @Path("collectionId") collectionId: String,
    ): BaseResponse<Boolean>

    // 콘텐츠 북마크 토글
    @POST("/api/v1/bookmarks/contents/{contentId}")
    suspend fun toggleContentBookmark(
        @Path("contentId") contentId: String,
    ): BaseResponse<Boolean>
}
