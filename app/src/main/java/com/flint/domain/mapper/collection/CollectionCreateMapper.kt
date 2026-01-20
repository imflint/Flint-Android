package com.flint.domain.mapper.collection

import com.flint.data.dto.collection.request.CollectionCreateRequestDto
import com.flint.domain.model.collection.CollectionCreateRequestModel

fun CollectionCreateRequestModel.toDto(): CollectionCreateRequestDto =
    CollectionCreateRequestDto(
        imageUrl = imageUrl,
        title = title,
        description = description,
        isPublic = isPublic,
        contentIds = contentIds
    )