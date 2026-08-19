package com.flint.android.domain.mapper.collection

import com.flint.android.data.dto.collection.request.CollectionReportRequestDto
import com.flint.android.domain.model.collection.CollectionReportRequestModel

fun CollectionReportRequestModel.toDto(): CollectionReportRequestDto =
    CollectionReportRequestDto(
        reasons = reasons,
        otherDetail = otherDetail,
    )
