package com.flint.presentation.collectiondetail.report

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CollectionReportViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<CollectionReportUiState> =
        MutableStateFlow(CollectionReportUiState())
    val uiState: StateFlow<CollectionReportUiState> = _uiState.asStateFlow()

    fun selectReportReason(reason: String) {
        _uiState.update {
            it.copy(
                selectedReportReason = reason,
                reportText = if (reason == ETC_REASON) it.reportText else "",
            )
        }
    }

    fun updateReportText(text: String) {
        _uiState.update { it.copy(reportText = text) }
    }

    companion object {
        private const val ETC_REASON = "기타"
    }
}
