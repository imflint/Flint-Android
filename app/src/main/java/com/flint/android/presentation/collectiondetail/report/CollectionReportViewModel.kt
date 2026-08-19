package com.flint.android.presentation.collectiondetail.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.flint.android.core.navigation.Route
import com.flint.android.domain.model.collection.CollectionReportRequestModel
import com.flint.android.domain.repository.CollectionRepository
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

    fun selectReportReason(reason: ReportReason) {
        _uiState.update {
            it.copy(
                selectedReportReason = reason,
                reportText = if (reason == ReportReason.OTHER) it.reportText else "",
            )
        }
    }

    fun updateReportText(text: String) {
        _uiState.update { it.copy(reportText = text) }
    }

    fun submitReport() {
        val state = _uiState.value
        if (state.isLoading) return

        val reasonCode = state.selectedReportReason?.code ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val requestModel = CollectionReportRequestModel(
                reasons = listOf(reasonCode),
                otherDetail = state.reportText.ifBlank { null },
            )

            collectionRepository.postCollectionReport(collectionId, requestModel)
                .onSuccess {
                    _sideEffect.emit(CollectionReportSideEffect.ReportSuccess)
                }
                .onFailure {
                    _sideEffect.emit(CollectionReportSideEffect.ReportFailure)
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
