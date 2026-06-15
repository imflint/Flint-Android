package com.flint.presentation.collectiondetail.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.core.navigation.Route
import com.flint.domain.mapper.collection.toDto
import com.flint.domain.model.collection.CollectionReportRequestModel
import com.flint.domain.repository.CollectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CollectionReportSideEffect {
    data object ReportSuccess : CollectionReportSideEffect

    data object ReportFailure : CollectionReportSideEffect
}

@HiltViewModel
class CollectionReportViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val collectionRepository: CollectionRepository,
) : ViewModel() {
    private val collectionId: String =
        savedStateHandle.toRoute<Route.CollectionReport>().collectionId

    private val _uiState: MutableStateFlow<CollectionReportUiState> =
        MutableStateFlow(CollectionReportUiState())
    val uiState: StateFlow<CollectionReportUiState> = _uiState.asStateFlow()

    private val _sideEffect: MutableSharedFlow<CollectionReportSideEffect> = MutableSharedFlow()
    val sideEffect: SharedFlow<CollectionReportSideEffect> = _sideEffect.asSharedFlow()

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

    fun submitReport() {
        val state = _uiState.value
        if (state.isLoading) return

        val reasonCode = REASON_CODE_MAP[state.selectedReportReason] ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val requestModel = CollectionReportRequestModel(
                reasons = listOf(reasonCode),
                otherDetail = state.reportText.ifBlank { null },
            )

            collectionRepository.postCollectionReport(collectionId, requestModel.toDto())
                .onSuccess {
                    _sideEffect.emit(CollectionReportSideEffect.ReportSuccess)
                }
                .onFailure {
                    _sideEffect.emit(CollectionReportSideEffect.ReportFailure)
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    companion object {
        private const val ETC_REASON = "기타"

        private val REASON_CODE_MAP: Map<String, String> = mapOf(
            "욕설·혐오 표현이 포함된 콘텐츠" to "ABUSE",
            "음란하거나 선정적인 콘텐츠" to "OBSCENE",
            "광고·홍보 또는 스팸성 콘텐츠" to "SPAM",
            "저작권을 침해한 콘텐츠" to "COPYRIGHT",
            ETC_REASON to "OTHER",
        )
    }
}
