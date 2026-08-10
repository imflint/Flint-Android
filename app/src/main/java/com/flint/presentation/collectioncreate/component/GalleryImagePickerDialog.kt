package com.flint.presentation.collectioncreate.component

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.image.NetworkImage
import com.flint.core.designsystem.component.indicator.FlintLoadingIndicator
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun GalleryImagePickerDialog(
    maxSelectable: Int,
    onConfirm: (List<Uri>) -> Unit,
    onDismiss: () -> Unit,
    initialSelectedImages: List<Uri> = emptyList(),
) {
    val context = LocalContext.current
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedImages by remember { mutableStateOf(initialSelectedImages) }

    LaunchedEffect(Unit) {
        images = withContext(Dispatchers.IO) { queryDeviceGalleryImages(context) }
        isLoading = false
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FlintTheme.colors.background),
        ) {
            FlintBackTopAppbar(
                onClick = onDismiss,
                title = "사진 선택 (최대 ${maxSelectable}장)",
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        FlintLoadingIndicator()
                    }
                }
                images.isEmpty() -> {
                    Box(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "사진이 없어요",
                            color = FlintTheme.colors.gray300,
                            style = FlintTheme.typography.body1R16,
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(2.dp),
                        modifier = Modifier.weight(1f),
                    ) {
                        items(images, key = { it.toString() }) { uri ->
                            val selectedOrder = selectedImages.indexOf(uri).let { index -> if (index >= 0) index + 1 else null }
                            val isDisabled = selectedOrder == null && selectedImages.size >= maxSelectable

                            GalleryImageGridItem(
                                uri = uri,
                                selectedOrder = selectedOrder,
                                isDisabled = isDisabled,
                                onClick = {
                                    selectedImages = when {
                                        selectedOrder != null -> selectedImages - uri
                                        selectedImages.size < maxSelectable -> selectedImages + uri
                                        else -> selectedImages
                                    }
                                },
                                modifier = Modifier.padding(2.dp),
                            )
                        }
                    }
                }
            }

            FlintLargeButton(
                text = if (selectedImages.isEmpty()) "추가" else "추가 (${selectedImages.size})",
                state = if (selectedImages.isNotEmpty()) FlintButtonState.Able else FlintButtonState.Disable,
                enabled = selectedImages.isNotEmpty(),
                onClick = { onConfirm(selectedImages) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}

@Composable
private fun GalleryImageGridItem(
    uri: Uri,
    selectedOrder: Int?,
    isDisabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(4.dp))
            .clickable(enabled = !isDisabled, onClick = onClick),
    ) {
        NetworkImage(
            imageUrl = uri,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        if (isDisabled) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FlintTheme.colors.overlay),
            )
        }

        SelectionOrderBadge(
            order = selectedOrder,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(6.dp),
        )
    }
}

@Composable
private fun SelectionOrderBadge(
    order: Int?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(if (order != null) Color(0xFF1ABFF2) else Color(0x66000000))
            .then(
                if (order == null) {
                    Modifier.border(BorderStroke(1.5.dp, Color.White), CircleShape)
                } else {
                    Modifier
                },
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (order != null) {
            Text(
                text = order.toString(),
                color = Color.White,
                style = FlintTheme.typography.caption1M12,
            )
        }
    }
}

private fun queryDeviceGalleryImages(context: Context): List<Uri> {
    val images = mutableListOf<Uri>()
    val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(collection, projection, null, null, sortOrder)?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (cursor.moveToNext()) {
            images.add(ContentUris.withAppendedId(collection, cursor.getLong(idColumn)))
        }
    }

    return images
}
