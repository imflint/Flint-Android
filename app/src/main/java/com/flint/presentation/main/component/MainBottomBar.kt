package com.flint.presentation.main.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.core.common.extension.noRippleClickable
import com.flint.core.designsystem.theme.FlintTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun MainBottomBar(
    tabs: ImmutableList<MainTab>,
    modifier: Modifier = Modifier,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
){
    Row (
        modifier = modifier.fillMaxWidth()
    ){
        tabs.forEach { tab ->
            key(tab.route) {
                MainBottomBarItem(
                    tab = tab,
                    selected = (tab == currentTab),
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }

}

@Composable
private fun MainBottomBarItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
){
    Column (
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .fillMaxWidth()
            .padding(vertical = 28.dp, horizontal = 9.5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Icon(
            imageVector = ImageVector.vectorResource(tab.icon),
            modifier = Modifier,
            contentDescription = tab.label,
        )
        Text(
            text = tab.label,
        )
    }
}

@Preview
@Composable
private fun MainBottomBarPreview() {
    FlintTheme {
        MainBottomBar(
            tabs = MainTab.entries.toImmutableList(),
            modifier = Modifier,
            currentTab = MainTab.HOME,
            onTabSelected = {},
        )
    }
}