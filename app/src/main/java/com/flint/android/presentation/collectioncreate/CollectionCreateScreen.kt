package com.flint.android.presentation.collectioncreate

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.flint.android.R
import com.flint.android.core.common.util.UiState
import com.flint.android.core.designsystem.component.bottomsheet.MenuBottomSheet
import com.flint.android.core.designsystem.component.bottomsheet.MenuBottomSheetData
import com.flint.android.core.designsystem.component.button.FlintButtonState
import com.flint.android.core.designsystem.component.button.FlintIconButton
import com.flint.android.core.designsystem.component.button.FlintLargeButton
import com.flint.android.core.designsystem.component.textfield.CollectionInputTextField
import com.flint.android.core.designsystem.component.toast.ShowToast
import com.flint.android.core.designsystem.component.topappbar.FlintBackTopAppbar
import com.flint.android.core.designsystem.interaction.flintIconClickable
import com.flint.android.core.designsystem.theme.FlintTheme
import com.flint.android.domain.model.search.SearchContentItemModel
import com.flint.android.domain.model.search.SearchContentListModel
import com.flint.android.presentation.collectioncreate.component.CollectionCreateContentDeleteModal
import com.flint.android.presentation.collectioncreate.component.CollectionCreateContentImage
import com.flint.android.presentation.collectioncreate.component.CollectionCreateContentReason
import com.flint.android.presentation.collectioncreate.component.CollectionCreateContentSection
import com.flint.android.presentation.collectioncreate.component.CollectionCreateLeaveModal
import com.flint.android.presentation.collectioncreate.component.CollectionCreateThumbnail
import com.flint.android.presentation.collectioncreate.component.CollectionEditLeaveModal
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Composable
fun CollectionCreateRoute(
    paddingValues: PaddingValues,
    navigateToAddContent: () -> Unit,
    navigateUp: () -> Unit,
    navigateToCollectionDetail: (collectionId: String) -> Unit,
    viewModel: CollectionCreateViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.createSuccess.collect { uistate ->
            when(uistate) {
                is UiState.Success -> {
                    navigateToCollectionDetail(uistate.data)
                    viewModel.resetCreateSuccess()
                }
                is UiState.Failure -> {
                    Toast.makeText(context, "저장에 실패했어요. 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    viewModel.resetCreateSuccess()
                }
                else -> {}
            }
        }
    }

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
                contract = remember { GmsCompatPickMultipleVisualMedia(maxItems) },
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
                    "작품 이미지는 최대 ${MAX_CONTENT_IMAGE_COUNT}개까지 추가할 수 있어요",
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
    var showValidationErrors by rememberSaveable { mutableStateOf(false) }
    var showLeaveConfirmModal by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf<String?>(null) }
    var toastRequestId by remember { mutableStateOf(0) }

    fun requestBack() {
        if (uiState.isDirty) showLeaveConfirmModal = true else onBackClick()
    }

    // 로컬 함수 참조(::requestBack)는 Kotlin의 CallableReference.equals()가 캡처된 상태가 아니라
    // 참조 대상 함수 자체만 비교하므로, 리컴포즈마다 새로 캡처된 uiState 를 Compose 가 "안 바뀜"으로
    // 오판해 콜백이 최초 진입 시점 상태에 고정될 수 있다. 람다로 감싸 매 리컴포즈마다 갱신되게 한다.
    BackHandler(onBack = { requestBack() })

    // 같은 문자열을 다시 대입하면 State 값이 안 바뀌어(구조적 동등성) 리컴포즈가 안 될 수 있으므로,
    // 매 요청마다 카운터를 증가시켜 ShowToast 의 LaunchedEffect 타이머가 항상 재시작되도록 한다.
    fun showToast(message: String) {
        toastMessage = message
        toastRequestId++
    }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // 필드 자체에 매달아 두면 리스트 구성이 바뀌어도(아이템 추가/삭제/순서 변경) 정확한 필드로 스크롤된다.
    // 다만 완료 버튼은 리스트 맨 아래에 있어서, 에러 필드가 화면 밖 -> 아직 컴포즈되지 않았을 수 있다.
    // 이 경우 bringIntoView() 가 실패하므로, 대략적인 인덱스로 먼저 스크롤해 컴포지션을 강제한다.
    // (인덱스가 다소 어긋나도 근처로만 가면 되므로, 이 하드코딩은 point 2 만큼 취약하지 않다.)
    val titleItemIndex = 1
    val publicItemIndex = 3
    val addContentHeaderIndex = 4
    val firstContentItemIndex = addContentHeaderIndex + 1

    val titleBringIntoViewRequester = remember { BringIntoViewRequester() }
    val publicBringIntoViewRequester = remember { BringIntoViewRequester() }
    val addContentBringIntoViewRequester = remember { BringIntoViewRequester() }
    val contentReasonBringIntoViewRequesters = remember { mutableMapOf<String, BringIntoViewRequester>() }
    fun reasonBringIntoViewRequester(contentId: String): BringIntoViewRequester =
        contentReasonBringIntoViewRequesters.getOrPut(contentId) { BringIntoViewRequester() }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = FlintTheme.colors.background)
        ) {
            FlintBackTopAppbar(onClick = { requestBack() })

            LazyColumn(
                state = lazyListState,
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
                        isError = showValidationErrors && uiState.title.isBlank(),
                        bringIntoViewRequester = titleBringIntoViewRequester,
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
                        isError = showValidationErrors && uiState.isPublic == null,
                        bringIntoViewRequester = publicBringIntoViewRequester,
                        modifier = Modifier.padding(horizontal = (16).dp),
                    )

                    Spacer(Modifier.height(20.dp))
                }

                // 작품 추가 섹션
                collectionAddContentSection(
                    selectedContents = uiState.selectedContents,
                    contentDetailsMap = uiState.contentDetailsMap,
                    showValidationErrors = showValidationErrors,
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
                    headerBringIntoViewRequester = addContentBringIntoViewRequester,
                    reasonBringIntoViewRequesterFor = ::reasonBringIntoViewRequester,
                )

                item {
                    Text(
                        text = "Flint에서 제공하는 영화 · 드라마를 포함한 모든 콘텐츠의 저작권은 각 권리자에게 있으며, 관련 법령에 따라 보호됩니다. 컬렉션 이용 시 저작권을 준수해 주세요.",
                        color = FlintTheme.colors.gray300,
                        style = FlintTheme.typography.caption1R12,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(Modifier.height(12.dp))
                }

                // 완료 버튼 - 고정되지 않고 스크롤해야 노출됨
                item {
                    FlintLargeButton(
                        text = "완료",
                        state = if (uiState.isLoading) FlintButtonState.Disable else FlintButtonState.Able,
                        onClick = {
                            when {
                                uiState.isFinishButtonEnabled -> {
                                    showValidationErrors = false
                                    onFinishClick()
                                }
                                !uiState.isRequiredFieldsFilled -> {
                                    showValidationErrors = true
                                    showToast("필수 항목을 모두 입력해주세요")

                                    val firstInvalidContentIndex = uiState.selectedContents.indexOfFirst {
                                        uiState.contentDetailsMap[it.id]?.reason.isNullOrBlank()
                                    }
                                    coroutineScope.launch {
                                        val (coarseIndex, requester) = when {
                                            uiState.title.isBlank() ->
                                                titleItemIndex to titleBringIntoViewRequester
                                            uiState.isPublic == null ->
                                                publicItemIndex to publicBringIntoViewRequester
                                            firstInvalidContentIndex >= 0 -> {
                                                val contentId = uiState.selectedContents[firstInvalidContentIndex].id
                                                (firstContentItemIndex + firstInvalidContentIndex) to
                                                    reasonBringIntoViewRequester(contentId)
                                            }
                                            else ->
                                                addContentHeaderIndex to addContentBringIntoViewRequester
                                        }
                                        // 에러 필드가 화면 밖이라 아직 컴포즈되지 않았을 수 있으므로 먼저 근처로 스크롤해
                                        // 컴포지션을 강제한 뒤, 정확한 위치는 bringIntoView() 가 보정한다.
                                        lazyListState.scrollToItem(coarseIndex)
                                        runCatching { requester.bringIntoView() }
                                    }
                                }
                                uiState.isLoading -> Unit
                                else -> showToast("변경된 내용이 없어요")
                            }
                        },
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                        enabled = !uiState.isLoading,
                    )
                }
            }
        }

        toastMessage?.let { message ->
            ShowToast(
                text = message,
                key = toastRequestId,
                imageVector = ImageVector.vectorResource(R.drawable.ic_toast_error),
                paddingValues = PaddingValues.Zero,
                yOffset = 120.dp,
                imeYOffset = 16.dp,
                hide = { toastMessage = null },
            )
        }
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

    if (showLeaveConfirmModal) {
        val onLeaveConfirm = {
            showLeaveConfirmModal = false
            onBackClick()
        }
        val onLeaveDismiss = { showLeaveConfirmModal = false }

        if (uiState.isEditMode) {
            CollectionEditLeaveModal(onConfirm = onLeaveConfirm, onDismiss = onLeaveDismiss)
        } else {
            CollectionCreateLeaveModal(onConfirm = onLeaveConfirm, onDismiss = onLeaveDismiss)
        }
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
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
){
    Column(modifier = modifier) {
        Text(
            text = "컬렉션 제목",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head3M18,
        )

        Spacer(Modifier.height(16.dp))

        CollectionInputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(bringIntoViewRequester),
            value = title,
            placeholder = "컬렉션의 제목을 입력해주세요.",
            onValueChanged = onTitleChanged,
            maxLength = 20,
            singleLine = true,
            maxLines = 1,
            isShowLengthTitle = true,
            isError = isError,
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
    bringIntoViewRequester: BringIntoViewRequester,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    Column(modifier = modifier) {
        Text(
            text = "컬렉션 공개 여부",
            color = FlintTheme.colors.white,
            style = FlintTheme.typography.head3M18,
        )

        Spacer(Modifier.height(16.dp))

        Row(modifier = Modifier.bringIntoViewRequester(bringIntoViewRequester)) {
            FlintIconButton(
                text = "공개",
                iconRes = R.drawable.ic_share,
                state = when (isPublic) {
                    true -> FlintButtonState.ColorOutline
                    false -> FlintButtonState.Disable
                    else -> if (isError) FlintButtonState.Error else FlintButtonState.Outline
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
                    else -> if (isError) FlintButtonState.Error else FlintButtonState.Outline
                },
                onClick = { onPublicChanged(false) },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(start = 8.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)
            )
        }
    }
}

private fun LazyListScope.collectionAddContentSection(
    selectedContents: ImmutableList<SearchContentItemModel>,
    contentDetailsMap: Map<String, ContentDetail>,
    onDeleteRequest: (SearchContentItemModel) -> Unit,
    onSpoilerChanged: (String, Boolean) -> Unit,
    onReasonChanged: (String, String) -> Unit,
    onAddContentClick: () -> Unit,
    onSelectContentImage: (contentId: String) -> Unit,
    onRemoveExistingContentImage: (contentId: String, index: Int) -> Unit,
    onRemoveContentImage: (contentId: String, index: Int) -> Unit,
    showValidationErrors: Boolean,
    headerBringIntoViewRequester: BringIntoViewRequester,
    reasonBringIntoViewRequesterFor: (contentId: String) -> BringIntoViewRequester,
) {
    item {
        val isContentCountError = showValidationErrors && selectedContents.size < 2

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .bringIntoViewRequester(headerBringIntoViewRequester),
        ) {
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
                    color = if (isContentCountError) FlintTheme.colors.error500 else FlintTheme.colors.gray200,
                    style = FlintTheme.typography.body2R14,
                )
                Text(
                    text = "${selectedContents.size}/$MAX_CONTENT_COUNT",
                    color = FlintTheme.colors.white,
                    style = FlintTheme.typography.body2R14,
                )
            }
        }
    }

    items(
        items = selectedContents,
        key = { content -> content.id },
    ) { content ->
        val detail = contentDetailsMap[content.id] ?: ContentDetail()

        // LazyColumn 의 verticalArrangement(16dp)와 합쳐 기존과 동일한 28dp 간격을 만든다.
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(12.dp))

            Icon(
                painter = painterResource(R.drawable.ic_deselect_large_pri),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .align(Alignment.End)
                    .flintIconClickable(onClick = { onDeleteRequest(content) })
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
                isError = showValidationErrors && detail.reason.isBlank(),
                modifier = Modifier.bringIntoViewRequester(reasonBringIntoViewRequesterFor(content.id)),
            )
        }
    }

    if (selectedContents.size < MAX_CONTENT_COUNT) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(Modifier.height(12.dp))

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

                Spacer(Modifier.height(4.dp))
            }
        }
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
