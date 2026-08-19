package com.flint.android.presentation.collectiondetail.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.android.R
import com.flint.android.core.designsystem.component.button.FlintButtonState
import com.flint.android.core.designsystem.component.button.FlintLargeButton
import com.flint.android.core.designsystem.theme.FlintTheme

@Composable
fun ReportBottomSection(
    isEnabled: Boolean,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ReportInformationSection()

        Spacer(modifier = Modifier.height(18.dp))

        FlintLargeButton(
            text = "제출",
            state = if (isEnabled) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = onSubmitClick,
            enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun ReportInformationSection(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = FlintTheme.colors.gray800, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 14.dp, horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        ReportInformationItem(
            content = "신고 전에 해당 콘텐츠가 신고 대상에 해당하는지 한 번 더 확인해 주세요."
        )
        ReportInformationItem(
            content = "신고된 콘텐츠는 플린트 이용 약관 및 운영 정책에 따라 검토되며, 필요 시 활동 제한 등의 조치가 적용될 수 있습니다."
        )
    }
}

@Composable
private fun ReportInformationItem(
    content: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            painter = painterResource(R.drawable.ic_small_info),
            contentDescription = null,
            tint = FlintTheme.colors.gray300,
        )
        Text(
            text = content,
            style = FlintTheme.typography.caption1M12,
            color = FlintTheme.colors.gray300,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ReportBottomSectionEnabledPreview() {
    FlintTheme {
        ReportBottomSection(
            isEnabled = true,
            onSubmitClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ReportBottomSectionDisabledPreview() {
    FlintTheme {
        ReportBottomSection(
            isEnabled = false,
            onSubmitClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun ReportInformationSectionPreview() {
    FlintTheme {
        ReportInformationSection()
    }
}
