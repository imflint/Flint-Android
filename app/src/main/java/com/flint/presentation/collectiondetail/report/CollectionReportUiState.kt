package com.flint.presentation.collectiondetail.report

data class CollectionReportUiState(
    val selectedReportReason: ReportReason? = null,
    val reportText: String = "",
    val isLoading: Boolean = false,
)
