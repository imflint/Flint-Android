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

    // 수정 모드에서는 원본과 비교하고, 작성 모드에서는 기본값(null/빈 값)과 비교한다.
    // originalXxx 필드들은 작성 모드에서 기본값을 가지므로 두 모드에서 동일한 식으로 계산할 수 있다.
    private val fieldsChanged: Boolean get() =
        title != (originalTitle ?: "") ||
        description != originalDescription ||
        isPublic != originalIsPublic ||
        thumbnailImageUri != null ||
        selectedContents.map { it.id }.toSet() != originalContentIds ||
        contentDetailsMap.any { (id, detail) ->
            val original = originalContentDetails[id]
            detail.isSpoiler != (original?.first ?: false) ||
            detail.reason != (original?.second ?: "") ||
            detail.contentImageUris.isNotEmpty()
        }

    // 수정 모드에서만 의미가 있는 필드(기존 썸네일/이미지 URL) 변경 여부.
    private val editModeFieldsChanged: Boolean get() = isEditMode && (
        existingThumbnailUrl != originalThumbnailUrl ||
        contentDetailsMap.any { (id, detail) ->
            detail.existingImageUrls != (originalContentImageUrls[id] ?: emptyList<String>())
        }
    )

    private val hasChanges: Boolean get() = isEditMode && (fieldsChanged || editModeFieldsChanged)

    // 뒤로가기 이탈 확인 팝업 노출 여부: 수정 모드는 원본 대비 변경 여부, 작성 모드는 입력 여부로 판단한다.
    val isDirty: Boolean get() = fieldsChanged || editModeFieldsChanged

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
