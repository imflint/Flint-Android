package com.flint.android.presentation.collectioncreate

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.flint.android.domain.model.search.SearchContentItemModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CollectionCreateUiState(
    val thumbnailImageUri: Uri? = null,
    val existingThumbnailUrl: String? = null,
    val title: String = "",
    val description: String = "",
    val isPublic: Boolean? = null,
    val selectedContents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val contentDetailsMap: Map<String, ContentDetail> = emptyMap(),
    val contents: ImmutableList<SearchContentItemModel> = persistentListOf(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    // 수정 모드 원본값 (null이면 생성 모드)
    val originalTitle: String? = null,
    val originalDescription: String = "",
    val originalIsPublic: Boolean? = null,
    val originalThumbnailUrl: String? = null,
    val originalContentIds: Set<String> = emptySet(),
    val originalContentDetails: Map<String, Pair<Boolean, String>> = emptyMap(),
    val originalContentImageUrls: Map<String, List<String>> = emptyMap(),
) {
    val isEditMode: Boolean get() = originalTitle != null

    private val hasChanges: Boolean get() = isEditMode && (
        title != originalTitle ||
        description != originalDescription ||
        isPublic != originalIsPublic ||
        thumbnailImageUri != null ||
        existingThumbnailUrl != originalThumbnailUrl ||
        selectedContents.map { it.id }.toSet() != originalContentIds ||
        contentDetailsMap.any { (id, detail) ->
            val original = originalContentDetails[id]
            detail.isSpoiler != original?.first ||
            detail.reason != original?.second ||
            detail.contentImageUris.isNotEmpty() ||
            detail.existingImageUrls != (originalContentImageUrls[id] ?: emptyList<String>())
        }
    )

    // 뒤로가기 이탈 확인 팝업 노출 여부: 수정 모드는 원본 대비 변경 여부, 작성 모드는 입력 여부로 판단한다.
    val isDirty: Boolean get() = if (isEditMode) {
        hasChanges
    } else {
        title.isNotBlank() ||
            description.isNotBlank() ||
            isPublic != null ||
            thumbnailImageUri != null ||
            selectedContents.isNotEmpty() ||
            contentDetailsMap.values.any { detail ->
                detail.isSpoiler || detail.reason.isNotBlank() || detail.contentImageUris.isNotEmpty()
            }
    }

    val isRequiredFieldsFilled: Boolean get() =
        title.isNotBlank() &&
        isPublic != null &&
        selectedContents.size >= 2 &&
        selectedContents.all { contentDetailsMap[it.id]?.reason?.isNotBlank() == true }

    val isFinishButtonEnabled: Boolean get() =
        !isLoading &&
        isRequiredFieldsFilled &&
        (!isEditMode || hasChanges)

    val isCancelModalVisible: Boolean =
        contentDetailsMap.values.any { it.reason.isNotBlank() }
}

@Immutable
data class ContentDetail(
    val isSpoiler: Boolean = false,
    val reason: String = "",
    val existingImageUrls: List<String> = emptyList(),
    val contentImageUris: List<Uri> = emptyList(),
) {
    val remainingImageSlots: Int get() =
        (MAX_CONTENT_IMAGE_COUNT - existingImageUrls.size - contentImageUris.size).coerceAtLeast(0)
}
