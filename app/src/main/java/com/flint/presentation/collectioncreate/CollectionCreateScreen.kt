package com.flint.presentation.collectioncreate

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flint.R
import com.flint.core.common.util.UiState
import com.flint.core.designsystem.component.bottomsheet.MenuBottomSheet
import com.flint.core.designsystem.component.bottomsheet.MenuBottomSheetData
import com.flint.core.designsystem.component.button.FlintButtonState
import com.flint.core.designsystem.component.button.FlintIconButton
import com.flint.core.designsystem.component.button.FlintLargeButton
import com.flint.core.designsystem.component.textfield.CollectionInputTextField
import com.flint.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.core.designsystem.theme.FlintTheme
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.model.search.SearchContentListModel
import com.flint.presentation.collectioncreate.component.CollectionCreateContentDeleteModal
import com.flint.presentation.collectioncreate.component.CollectionCreateContentImage
import com.flint.presentation.collectioncreate.component.CollectionCreateContentReason
import com.flint.presentation.collectioncreate.component.CollectionCreateContentSection
import com.flint.presentation.collectioncreate.component.CollectionCreateThumbnail
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Composable
fun CollectionCreateRoute(
    paddingValues: PaddingValues,
    navigateToAddContent: () -> Unit,
    navigateUp: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    viewModel: CollectionCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.createSuccess.collect { uistate ->
            when(uistate) {
                is UiState.Success -> {
                    navigateToCollectionDetail(uistate.data)
                    viewModel.resetCreateSuccess()
                }
                else -> {}
            }
        }
    }

    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        viewModel.updateThumbnailImageUri(uri)
    }

    var pendingContentId by rememberSaveable { mutableStateOf<String?>(null) }

    val onContentImagesPicked: (List<Uri>) -> Unit = { uris ->
        pendingContentId?.let { contentId -> viewModel.addContentImageUris(contentId, uris) }
        pendingContentId = null
    }

    // 시스템 포토피커는 maxItems 가 1보다 커야 하므로, 남은 자리 수별 런처를 미리 등록해 두고 골라 쓴다.
    val multipleImagePickers = (2..MAX_CONTENT_IMAGE_COUNT).associateWith { maxItems ->
        key(maxItems) {
            rememberLauncherForActivityResult(
                contract = remember { ActivityResultContracts.PickMultipleVisualMedia(maxItems) },
                onResult = onContentImagesPicked,
            )
        }
    }

    val singleImagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri: Uri? ->
        onContentImagesPicked(listOfNotNull(uri))
    }

    CollectionCreateScreen(
        uiState = uiState,
        onTitleChanged = viewModel::updateTitle,
        onDescriptionChanged = viewModel::updateDescription,
        onPublicChanged = viewModel::updateIsPublic,
        onRemoveContent = viewModel::removeContent,
        onBackClick = navigateUp,
        onSpoilerChanged = viewModel::updateSpoiler,
        onReasonChanged = viewModel::updateReason,
        onAddContentClick = navigateToAddContent,
        onFinishClick = viewModel::onClickFinish,
        onGalleryClick = { galleryLauncher.launch("image/*") },
        onThumbnailDelete = viewModel::deleteThumbnail,
        onSelectContentImage = { contentId ->
            val remainingSlots = (uiState.contentDetailsMap[contentId] ?: ContentDetail()).remainingImageSlots
            val imageOnlyRequest = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            when {
                remainingSlots == 0 -> Toast.makeText(
                    context,
                    "사진은 최대 ${MAX_CONTENT_IMAGE_COUNT}장까지 등록할 수 있어요",
                    Toast.LENGTH_SHORT,
                ).show()

                remainingSlots == 1 -> {
                    pendingContentId = contentId
                    singleImagePicker.launch(imageOnlyRequest)
                }

                else -> {
                    pendingContentId = contentId
                    multipleImagePickers.getValue(remainingSlots).launch(imageOnlyRequest)
                }
            }
        },
        onRemoveExistingContentImage = viewModel::removeExistingContentImageUrl,
        onRemoveContentImage = viewModel::removeContentImageUri,
        modifier = Modifier.padding(paddingValues),
    )
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun CollectionCreateScreen(
    uiState: CollectionCreateUiState,
    onTitleChanged: (String) -> Unit = {},
    onDescriptionChanged: (String) -> Unit = {},
    onPublicChanged: (Boolean?) -> Unit = {},
    onRemoveContent: (SearchContentItemModel) -> Unit,
    onBackClick: () -> Unit,
    onSpoilerChanged: (String, Boolean) -> Unit = { _, _ -> },
    onReasonChanged: (String, String) -> Unit = { _, _ -> },
    onAddContentClick: () -> Unit,
    onFinishClick: () -> Unit,
    onGalleryClick: () -> Unit = {},
    onThumbnailDelete: () -> Unit = {},
    onSelectContentImage: (contentId: String) -> Unit = {},
    onRemoveExistingContentImage: (contentId: String, index: Int) -> Unit = { _, _ -> },
    onRemoveContentImage: (contentId: String, index: Int) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    var isModalVisible by remember { mutableStateOf(false) }
    var contentToDelete by remember { mutableStateOf<SearchContentItemModel?>(null) }
    var isThumbnailBottomSheetVisible by remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = FlintTheme.colors.background)
    ) {
        FlintBackTopAppbar(onClick = onBackClick)

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // 썸네일
            item {
                CollectionCreateThumbnail(
                    imageUrl = uiState.thumbnailImageUri ?: uiState.existingThumbnailUrl,
                    onClick = { isThumbnailBottomSheetVisible = true },
                )

                Spacer(Modifier.height(20.dp))
            }

            // 컬렉션 제목
            item {
                CollectionTitle(
                    title = uiState.title,
                    onTitleChanged = onTitleChanged,
                    modifier = Modifier.padding(horizontal = (16).dp),
                )
            }

            // 컬렉션 소개
            item {
                CollectionDescription(
                    description = uiState.description,
                    onDescriptionChanged = onDescriptionChanged,
                    modifier = Modifier.padding(horizontal = (16).dp),
                )
            }

            // 컬렉션 공개 여부
            item {
                CollectionPublicSection(
                    isPublic = uiState.isPublic,
                    onPublicChanged = onPublicChanged,
                    modifier = Modifier.padding(horizontal = (16).dp),
                )

                Spacer(Modifier.height(20.dp))
            }

            // 작품 추가 섹션
            item {
                CollectionAddContentSection(
                    selectedContents = uiState.selectedContents,
                    contentDetailsMap = uiState.contentDetailsMap,
                    onDeleteRequest = { content ->
                        contentToDelete = content
                        isModalVisible = true
                    },
                    onSpoilerChanged = onSpoilerChanged,
                    onReasonChanged = onReasonChanged,
                    onAddContentClick = onAddContentClick,
                    onSelectContentImage = onSelectContentImage,
                    onRemoveExistingContentImage = onRemoveExistingContentImage,
                    onRemoveContentImage = onRemoveContentImage,
                    modifier = Modifier.padding(horizontal = (16).dp),
                )

                Spacer(Modifier.height(20.dp))
            }

            item {
                Text(
                    text = "Flint에서 제공하는 영화 · 드라마를 포함한 모든 콘텐츠의 저작권은 각 권리자에게 있으며, 관련 법령에 따라 보호됩니다. \n컬렉션 이용 시 저작권을 준수해 주세요.",
                    color = FlintTheme.colors.gray300,
                    style = FlintTheme.typography.caption1R12,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(Modifier.height(12.dp))
            }
        }

        FlintLargeButton(
            text = "완료",
            state = if (uiState.isFinishButtonEnabled) FlintButtonState.Able else FlintButtonState.Disable,
            onClick = {
                onFinishClick()
          },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            enabled = uiState.isFinishButtonEnabled,
        )
    }


    if (isModalVisible) {
        CollectionCreateContentDeleteModal(
            onConfirm = {
                contentToDelete?.let { onRemoveContent(it) }
                contentToDelete = null
                isModalVisible = false
            },
            onDismiss = {
                contentToDelete = null
                isModalVisible = false
            },
        )
    }

    if (isThumbnailBottomSheetVisible) {
        val menuBottomSheetDataList =
            listOf(
                MenuBottomSheetData(
                    label = "갤러리에서 선택",
                    clickAction = onGalleryClick,
                ),
                MenuBottomSheetData(
                    label = "커버 사진 삭제",
                    color = FlintTheme.colors.error500,
                    clickAction = onThumbnailDelete,
                ),
            )

        val sheetState = rememberModalBottomSheetState()

        MenuBottomSheet(
            menuBottomSheetDataList = menuBottomSheetDataList,
            onDismiss = { isThumbnailBottomSheetVisible = false },
            sheetState = sheetState,
        )
    }
}

@Composable
private fun CollectionTitle(
    title: String,
    onTitleChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
){
    Column(modifier = modifier) {
        Text(
            text = "컬렉션 제목",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head3M18,
        )

        Spacer(Modifier.height(16.dp))

        CollectionInputTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            placeholder = "컬렉션의 제목을 입력해주세요.",
            onValueChanged = onTitleChanged,
            maxLength = 20,
            singleLine = true,
            maxLines = 1,
            isShowLengthTitle = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
    }
}

@Composable
private fun CollectionDescription(
    description: String,
    onDescriptionChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text =
                buildAnnotatedString {
                    append("컬렉션 소개 ")
                    withStyle(
                        style = SpanStyle(color = FlintTheme.colors.gray300),
                    ) {
                        append("(선택)")
                    }
                },
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head3M18,
        )

        Spacer(Modifier.height(16.dp))

        CollectionInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 104.dp),
            value = description,
            placeholder = "컬렉션의 소개를 작성해주세요.",
            onValueChanged = onDescriptionChanged,
            maxLength = 45,
            singleLine = false,
            maxLines = Int.MAX_VALUE,
            isShowLengthTitle = true,
        )
    }
}

@Composable
private fun CollectionPublicSection(
    isPublic: Boolean?,
    onPublicChanged: (Boolean?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "컬렉션 공개 여부",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head3M18,
        )

        Spacer(Modifier.height(16.dp))

        Row {
            FlintIconButton(
                text = "공개",
                iconRes = R.drawable.ic_share,
                state = when (isPublic) {
                    true -> FlintButtonState.ColorOutline
                    false -> FlintButtonState.Disable
                    else -> FlintButtonState.Outline
                },
                onClick = { onPublicChanged(true) },
                modifier = Modifier.weight(1f),
            )

            Spacer(Modifier.width(8.dp))

            FlintIconButton(
                text = "비공개",
                iconRes = R.drawable.ic_lock,
                state = when (isPublic) {
                    true -> FlintButtonState.Disable
                    false -> FlintButtonState.ColorOutline
                    else -> FlintButtonState.Outline
                },
                onClick = { onPublicChanged(false) },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 8.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)
            )
        }
    }
}

@Composable
private fun CollectionAddContentSection(
    selectedContents: ImmutableList<SearchContentItemModel>,
    contentDetailsMap: Map<String, ContentDetail>,
    onDeleteRequest: (SearchContentItemModel) -> Unit,
    onSpoilerChanged: (String, Boolean) -> Unit,
    onReasonChanged: (String, String) -> Unit,
    onAddContentClick: () -> Unit,
    onSelectContentImage: (contentId: String) -> Unit,
    onRemoveExistingContentImage: (contentId: String, index: Int) -> Unit,
    onRemoveContentImage: (contentId: String, index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "작품 추가",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head3M18,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "작품을 2개 이상 추가해주세요.",
                color = FlintTheme.colors.gray200,
                style = FlintTheme.typography.body2R14,
            )
            Text(
                text = "${selectedContents.size}/10",
                color = FlintTheme.colors.white,
                style = FlintTheme.typography.body2R14,
            )
        }

        selectedContents.forEach { content ->
            val detail = contentDetailsMap[content.id] ?: ContentDetail()

            Spacer(Modifier.height(28.dp))

            Icon(
                painter = painterResource(R.drawable.ic_deselect_large_pri),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable(onClick = { onDeleteRequest(content) })
                    .padding(vertical = 10.dp)
                    .size(28.dp),
            )

            CollectionCreateContentSection(
                posterImageUrl = content.posterUrl,
                title = content.title,
                director = content.author,
                createdYear = content.year,
            )

            Spacer(Modifier.height(16.dp))

            if (detail.existingImageUrls.isNotEmpty() || detail.contentImageUris.isNotEmpty()) {
                CollectionCreateContentImage(
                    existingImageUrls = detail.existingImageUrls,
                    imageUris = detail.contentImageUris,
                    onDeleteExistingClick = { index -> onRemoveExistingContentImage(content.id, index) },
                    onDeleteClick = { index -> onRemoveContentImage(content.id, index) },
                )

                Spacer(Modifier.height(16.dp))
            }

            CollectionCreateContentReason(
                selectedReason = detail.reason,
                onSelectedReasonChanged = { reason -> onReasonChanged(content.id, reason) },
                onSelectImageClick = { onSelectContentImage(content.id) },
                isSpoiler = detail.isSpoiler,
                onSpoilerChanged = { isSpoiler -> onSpoilerChanged(content.id, isSpoiler) },
            )
        }

        Spacer(Modifier.height(28.dp))

        FlintIconButton(
            text = "작품 추가하기",
            iconRes = R.drawable.ic_plus,
            state = FlintButtonState.ColorOutline,
            onClick = onAddContentClick,
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 80.dp),
            contentPadding = PaddingValues(vertical = 28.dp)
        )
    }
}

@Preview()
@Composable
private fun CollectionCreateScreenPreview() {
    val fakeContents = SearchContentListModel.FakeList.take(2).toImmutableList()

    FlintTheme {
        CollectionCreateScreen(
            uiState = CollectionCreateUiState(
                title = "내 컬렉션",
                description = "컬렉션 소개입니다.",
                isPublic = true,
                selectedContents = fakeContents,
                contentDetailsMap = fakeContents.associate {
                    it.id to ContentDetail(
                        contentImageUris = listOf(
                            Uri.parse("https://example.com/1"),
                            Uri.parse("https://example.com/2"),
                        )
                    )
                },
            ),
            onRemoveContent = {},
            onBackClick = {},
            onAddContentClick = {},
            onFinishClick = {},
        )
    }
}
