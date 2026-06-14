package com.flint.presentation.collectiondetail.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectiondetail.report.component.ReportBottomSection
import com.flint.presentation.collectiondetail.report.component.ReportCheck
import com.flint.presentation.collectiondetail.report.component.ReportTopAppBar

@Composable
fun CollectionReportRoute(
    navigateUp: () -> Unit,
    viewModel: CollectionReportViewModel = hiltViewModel(),
) {
    val uiState: CollectionReportUiState by viewModel.uiState.collectAsStateWithLifecycle()

    CollectionReportScreen(
        selectedReportReason = uiState.selectedReportReason,
        reportText = uiState.reportText,
        onReportReasonSelected = viewModel::selectReportReason,
        onReportTextChanged = viewModel::updateReportText,
        onCancelClick = navigateUp,
        onSubmitClick = {},
    )
}

@Composable
private fun CollectionReportScreen(
    selectedReportReason: String?,
    reportText: String,
    onReportReasonSelected: (String) -> Unit,
    onReportTextChanged: (String) -> Unit,
    onCancelClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FlintTheme.colors.background),
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

        ReportBottomSection(
            isEnabled = selectedReportReason != null,
            onSubmitClick = onSubmitClick,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionReportScreenPreview() {
    FlintTheme {
        var selectedReportReason: String? by remember { mutableStateOf(null) }
        var reportText: String by remember { mutableStateOf("") }

        CollectionReportScreen(
            selectedReportReason = selectedReportReason,
            reportText = reportText,
            onReportReasonSelected = { selectedReportReason = it },
            onReportTextChanged = { reportText = it },
            onCancelClick = {},
            onSubmitClick = {},
        )
    }
}
