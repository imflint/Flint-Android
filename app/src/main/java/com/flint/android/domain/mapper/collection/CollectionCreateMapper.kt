package com.flint.android.domain.mapper.collection

import com.flint.android.data.dto.collection.request.CollectionCreateRequestDto
import com.flint.android.data.dto.collection.response.CollectionCreateResponseDto
import com.flint.android.domain.model.collection.CollectionCreateContentModel
import com.flint.android.domain.model.collection.CollectionCreateModel
import com.flint.android.domain.model.collection.CollectionCreateRequestModel

fun CollectionCreateRequestModel.toDto(): CollectionCreateRequestDto =
    CollectionCreateRequestDto(
        imageUrl = imageUrl,
        title = title,
        description = description,
        isPublic = isPublic,
        contentList = contentList.map { it.toDto() },
    )

private fun CollectionCreateContentModel.toDto(): CollectionCreateRequestDto.Content =
    CollectionCreateRequestDto.Content(
        contentId = contentId,
        isSpoiler = isSpoiler,
        reason = reason,
        imageUrls = imageUrls,
    )

fun CollectionCreateResponseDto.toModel(): CollectionCreateModel =
    CollectionCreateModel(
        collectionId = collectionId,
    )