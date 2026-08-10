package com.flint.presentation.collectioncreate

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.flint.domain.model.search.SearchContentItemModel
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
    private val isEditMode: Boolean get() = originalTitle != null

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

    val isFinishButtonEnabled: Boolean get() =
        !isLoading &&
        title.isNotBlank() &&
        isPublic != null &&
        selectedContents.size >= 2 &&
        (!isEditMode || hasChanges) &&
        selectedContents.all { contentDetailsMap[it.id]?.reason?.isNotBlank() == true }

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
