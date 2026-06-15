package com.flint.domain.mapper.collection

import com.flint.data.dto.collection.request.CollectionReportRequestDto
import com.flint.domain.model.collection.CollectionReportRequestModel

fun CollectionReportRequestModel.toDto(): CollectionReportRequestDto =
    CollectionReportRequestDto(
        reasons = reasons,
        otherDetail = otherDetail,
    )
