package com.flint.data.api

import com.flint.data.dto.base.BaseResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface BookmarkApi {
    @POST("api/v1/bookmarks/collections/{collectionId}")
    suspend fun toggleCollectionBookmark(
        @Path("collectionId") collectionId: String,
    ): BaseResponse<Boolean>
}
