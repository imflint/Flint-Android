package com.flint.presentation.onboarding.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.designsystem.component.progressbar.FlintProgressBar
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun StepProgressBar(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier,
) {
    val progress = (currentStep.toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        FlintProgressBar(
            progress = progress,
            modifier = Modifier.weight(1f),
        )

        Text(
            text = "$currentStep/$totalSteps",
            style = FlintTheme.typography.caption1M12,
            color = FlintTheme.colors.white,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun StepProgressBarPreview() {
    FlintTheme {
        StepProgressBar(
            currentStep = 1,
            totalSteps = 7,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun StepProgressBarStep3Preview() {
    FlintTheme {
        StepProgressBar(
            currentStep = 3,
            totalSteps = 7,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF121212)
@Composable
private fun StepProgressBarStep7Preview() {
    FlintTheme {
        StepProgressBar(
            currentStep = 7,
            totalSteps = 7,
        )
    }
}
