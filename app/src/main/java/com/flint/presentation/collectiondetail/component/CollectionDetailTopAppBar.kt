package com.flint.presentation.collectiondetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flint.R
import com.flint.core.designsystem.interaction.flintIconClickable
import com.flint.core.designsystem.theme.FlintTheme

@Composable
fun CollectionDetailTopAppBar(
    isMine: Boolean,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onReportClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = FlintTheme.colors.background,
) {
    var showSettingsMenu: Boolean by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier
                .size(48.dp)
                .flintIconClickable(onClick = onBackClick)
                .padding(12.dp),
            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
            contentDescription = stringResource(R.string.cd_back),
            tint = FlintTheme.colors.white,
        )

        Box(
            modifier = Modifier.size(48.dp),
        ) {
            Icon(
                modifier = Modifier
                    .size(48.dp)
                    .flintIconClickable(onClick = { showSettingsMenu = true }),
                imageVector = ImageVector.vectorResource(R.drawable.ic_kebab),
                contentDescription = stringResource(R.string.cd_more_options),
                tint = FlintTheme.colors.white,
            )

            DropdownMenu(
                expanded = showSettingsMenu,
                onDismissRequest = { showSettingsMenu = false },
                containerColor = Color.Transparent,
                shape = RectangleShape,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
            ) {
                if (isMine) {
                    CollectionEditDeleteDropdownMenuItem(
                        onEditClick = {
                            showSettingsMenu = false
                            onEditClick()
                        },
                        onDeleteClick = {
                            showSettingsMenu = false
                            onDeleteClick()
                        },
                    )
                } else {
                    CollectionReportDropdownMenuItem(
                        onClick = {
                            showSettingsMenu = false
                            onReportClick()
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CollectionDetailTopAppBarPreview() {
    FlintTheme {
        Column {
            CollectionDetailTopAppBar(
                isMine = true,
                onBackClick = {},
                onEditClick = {},
                onDeleteClick = {},
                onReportClick = {},
            )
            CollectionDetailTopAppBar(
                isMine = false,
                onBackClick = {},
                onEditClick = {},
                onDeleteClick = {},
                onReportClick = {},
            )
        }
    }
}
