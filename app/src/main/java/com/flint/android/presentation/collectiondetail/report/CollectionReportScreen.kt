package com.flint.android.presentation.collectiondetail.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.android.core.designsystem.component.toast.ShowToast
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.presentation.collectiondetail.report.component.ReportBottomSection
import com.flint.android.presentation.collectiondetail.report.component.ReportCheck
import com.flint.android.presentation.collectiondetail.report.component.ReportTopAppBar

@Composable
fun CollectionReportRoute(
    paddingValues: PaddingValues,
    navigateUp: () -> Unit,
    navigateUpWithSuccess: () -> Unit,
    viewModel: CollectionReportViewModel = hiltViewModel(),
) {
    val uiState: CollectionReportUiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFailureToast by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { event: CollectionReportSideEffect ->
            when (event) {
                CollectionReportSideEffect.ReportSuccess -> navigateUpWithSuccess()
                CollectionReportSideEffect.ReportFailure -> showFailureToast = true
            }
        }
    }

    CollectionReportScreen(
        paddingValues = paddingValues,
        isLoading = uiState.isLoading,
        selectedReportReason = uiState.selectedReportReason,
        reportText = uiState.reportText,
        onReportReasonSelected = viewModel::selectReportReason,
        onReportTextChanged = viewModel::updateReportText,
        onCancelClick = navigateUp,
        onSubmitClick = viewModel::submitReport,
    )

    if (showFailureToast) {
        ShowToast(
            text = "신고 접수에 실패했어요. 다시 시도해주세요.",
            imageVector = null,
            paddingValues = paddingValues,
            yOffset = 12.dp,
            hide = { showFailureToast = false },
        )
    }
}

@Composable
private fun CollectionReportScreen(
    paddingValues: PaddingValues,
    isLoading: Boolean,
    selectedReportReason: ReportReason?,
    reportText: String,
    onReportReasonSelected: (ReportReason) -> Unit,
    onReportTextChanged: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FlintTheme.colors.background)
            .padding(paddingValues),
    ) {
        ReportTopAppBar(
            onCancelClick = onCancelClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            item {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "이 컬렉션을 신고하시겠어요?",
                        style = FlintTheme.typography.head1Sb22,
                        color = FlintTheme.colors.white,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "신고해주신 내용은 검토를 통해 반영됩니다.",
                        style = FlintTheme.typography.body1M16,
                        color = FlintTheme.colors.white,
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            item {
                ReportCheck(
                    selectedReportReason = selectedReportReason,
                    onReportReasonSelected = onReportReasonSelected,
                    reportText = reportText,
                    onReportTextChanged = onReportTextChanged,
                )
            }
        }

        val isEnabled = !isLoading && when (selectedReportReason) {
            null -> false
            ReportReason.OTHER -> reportText.trim().length >= 2
            else -> true
        }

        ReportBottomSection(
            isEnabled = isEnabled,
            onSubmitClick = onSubmitClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionReportScreenPreview() {
    FlintTheme {
        var selectedReportReason: ReportReason? by remember { mutableStateOf(null) }
        var reportText: String by remember { mutableStateOf("") }

        CollectionReportScreen(
            paddingValues = PaddingValues(),
            isLoading = false,
            selectedReportReason = selectedReportReason,
            reportText = reportText,
            onReportReasonSelected = { selectedReportReason = it },
            onReportTextChanged = { reportText = it },
            onCancelClick = {},
            onSubmitClick = {},
        )
    }
}
