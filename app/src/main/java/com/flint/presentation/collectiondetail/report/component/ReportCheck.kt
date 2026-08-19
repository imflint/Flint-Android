package com.flint.presentation.collectiondetail.report.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.interaction.flintCardClickable
import com.flint.core.designsystem.component.textfield.CollectionInputTextField
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.presentation.collectiondetail.report.ReportReason

@Composable
fun ReportCheck(
    selectedReportReason: ReportReason?,
    onReportReasonSelected: (ReportReason) -> Unit,
    reportText: String,
    onReportTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        ReportReason.entries.forEach { reason ->
            ReportCheckItem(
                isChecked = selectedReportReason == reason,
                onCheckClick = { onReportReasonSelected(reason) },
                reportContent = reason.displayText,
            )
        }
        CollectionInputTextField(
            value = reportText,
            onValueChanged = onReportTextChanged,
            maxLength = 200,
            placeholder = "신고 사유를 작성해주세요.",
            singleLine = false,
            maxLines = Int.MAX_VALUE,
            isShowLengthTitle = true,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .heightIn(min = 104.dp)
                .onFocusChanged {
                    if (it.hasFocus) onReportReasonSelected(ReportReason.OTHER)
                }
        )
    }
}

@Composable
private fun ReportCheckItem(
    isChecked: Boolean,
    onCheckClick: () -> Unit,
    reportContent: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .flintCardClickable(onClick = onCheckClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = ImageVector.vectorResource(if (isChecked) R.drawable.ic_check_fill else R.drawable.ic_check_empty),
            contentDescription = null,
            tint = Color.Unspecified,
        )
        Text(
            text = reportContent,
            style = FlintTheme.typography.body1M16,
            color = FlintTheme.colors.white,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ReportCheckPreview() {
    FlintTheme {
        var selectedReportReason by remember { mutableStateOf<ReportReason?>(null) }
        var reportText by remember { mutableStateOf("") }

        ReportCheck(
            selectedReportReason = selectedReportReason,
            onReportReasonSelected = { selectedReportReason = it },
            reportText = reportText,
            onReportTextChanged = { reportText = it },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ReportCheckItemPreview() {
    FlintTheme {
        ReportCheckItem(
            isChecked = false,
            onCheckClick = {},
            reportContent = "욕설·혐오 표현이 포함된 콘텐츠",
        )
    }
}
