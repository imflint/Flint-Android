package com.flint.presentation.collectioncreate

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.domain.mapper.collection.toDto
import com.flint.domain.model.collection.CollectionCreateContentModel
import com.flint.domain.model.collection.CollectionCreateRequestModel
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.repository.CollectionRepository
import com.flint.domain.repository.SearchRepository
import com.flint.domain.repository.StorageRepository
import com.flint.domain.type.FileExtension
import com.flint.domain.type.StoragePathType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

import javax.inject.Inject

@HiltViewModel
class CollectionCreateViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val collectionRepository: CollectionRepository,
    private val searchRepository: SearchRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {

    private val editingCollectionId: String? = savedStateHandle["collectionId"]
    val isEditMode: Boolean = editingCollectionId != null
    private val _uiState = MutableStateFlow(CollectionCreateUiState())
    val uiState: StateFlow<CollectionCreateUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    private val _createSuccess = MutableStateFlow<UiState<String>>(UiState.Loading)
    val createSuccess = _createSuccess.asStateFlow()


    init {
        observeSearchQuery()
        loadInitialContents()
        if (editingCollectionId != null) {
            loadCollectionForEdit(editingCollectionId)
        }
    }

    fun onClickFinish() {
        if (_uiState.value.isLoading) return
        if (editingCollectionId != null) {
            putCollectionUpdate(editingCollectionId)
        } else {
            postCollectionCreate()
        }
    }

    private fun postCollectionCreate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val thumbnailKey = uploadImageIfNeeded(_uiState.value.thumbnailImageUri, StoragePathType.COLLECTION_THUMBNAIL)
                    .getOrElse {
                        _createSuccess.emit(UiState.Failure)
                        return@launch
                    }
                val contentImageKeysMap = uploadContentImagesIfNeeded()
                    .getOrElse {
                        _createSuccess.emit(UiState.Failure)
                        return@launch
                    }
                val requestModel = CollectionCreateRequestModel(
                    imageUrl = thumbnailKey ?: "",
                    title = uiState.value.title,
                    description = uiState.value.description.ifBlank { "" },
                    isPublic = uiState.value.isPublic ?: true,
                    contentList = uiState.value.selectedContents.map { content ->
                        val detail = uiState.value.contentDetailsMap[content.id] ?: ContentDetail()
                        CollectionCreateContentModel(
                            contentId = content.id,
                            isSpoiler = detail.isSpoiler,
                            reason = detail.reason.ifBlank { "" },
                            imageUrls = contentImageKeysMap[content.id] ?: emptyList(),
                        )
                    },
                )

                collectionRepository
                    .postCollectionCreate(requestModel.toDto())
                    .onSuccess {
                        println("컬렉션 생성 성공")
                        _createSuccess.emit(UiState.Success(it.collectionId))
                    }
                    .onFailure { e -> println("컬렉션 생성 실패: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadCollectionForEdit(collectionId: String) {
        viewModelScope.launch {
            collectionRepository.getCollectionDetail(collectionId)
                .onSuccess { detail ->
                    val selectedContents = detail.contents.map { content ->
                        SearchContentItemModel(
                            id = content.id,
                            title = content.title,
                            author = content.director,
                            posterUrl = content.imageUrl,
                            year = content.year,
                        )
                    }.toImmutableList()

                    val contentDetailsMap = detail.contents.associate { content ->
                        content.id to ContentDetail(
                            isSpoiler = content.isSpoiler,
                            reason = content.reason,
                            existingImageUrls = content.customImageUrls,
                        )
                    }

                    val thumbnailUrl = detail.thumbnailUrl.ifBlank { null }
                    val originalDetails = detail.contents.associate { content ->
                        content.id to Pair(content.isSpoiler, content.reason)
                    }
                    val originalImageUrls = detail.contents.associate { content ->
                        content.id to content.customImageUrls
                    }

                    _uiState.update {
                        it.copy(
                            existingThumbnailUrl = thumbnailUrl,
                            title = detail.title,
                            description = detail.description,
                            isPublic = detail.isPublic,
                            selectedContents = selectedContents,
                            contentDetailsMap = contentDetailsMap,
                            originalTitle = detail.title,
                            originalDescription = detail.description,
                            originalIsPublic = detail.isPublic,
                            originalThumbnailUrl = thumbnailUrl,
                            originalContentIds = detail.contents.map { it.id }.toSet(),
                            originalContentDetails = originalDetails,
                            originalContentImageUrls = originalImageUrls,
                        )
                    }
                }
        }
    }

    private fun putCollectionUpdate(collectionId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val thumbnailKey = if (_uiState.value.thumbnailImageUri != null) {
                    uploadImageIfNeeded(_uiState.value.thumbnailImageUri, StoragePathType.COLLECTION_THUMBNAIL)
                        .getOrElse {
                            _createSuccess.emit(UiState.Failure)
                            return@launch
                        }
                } else {
                    null
                }

                val contentImageKeysMap = uploadContentImagesIfNeeded()
                    .getOrElse {
                        _createSuccess.emit(UiState.Failure)
                        return@launch
                    }

                val requestModel = CollectionCreateRequestModel(
                    imageUrl = thumbnailKey ?: _uiState.value.existingThumbnailUrl ?: "",
                    title = _uiState.value.title,
                    description = _uiState.value.description.ifBlank { "" },
                    isPublic = _uiState.value.isPublic ?: true,
                    contentList = _uiState.value.selectedContents.map { content ->
                        val detail = _uiState.value.contentDetailsMap[content.id] ?: ContentDetail()
                        CollectionCreateContentModel(
                            contentId = content.id,
                            isSpoiler = detail.isSpoiler,
                            reason = detail.reason.ifBlank { "" },
                            imageUrls = contentImageKeysMap[content.id] ?: emptyList(),
                        )
                    },
                )

                collectionRepository
                    .updateCollection(collectionId, requestModel.toDto())
                    .onSuccess {
                        _createSuccess.emit(UiState.Success(collectionId))
                    }
                    .onFailure { e -> Timber.e(e, "컬렉션 수정 실패") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun resetCreateSuccess() = viewModelScope.launch {
        _createSuccess.emit(UiState.Empty)
    }

    fun resetSearchText() {
        searchQuery.value = ""
        _uiState.update { state ->
            state.copy(searchText = "")
        }
    }

    fun updateTitle(title: String) {
        _uiState.update { state ->
            state.copy(title = title)
        }
    }

    fun updateDescription(description: String) {
        _uiState.update { state ->
            state.copy(description = description)
        }
    }

    fun updateIsPublic(isPublic: Boolean?) {
        _uiState.update { state ->
            state.copy(isPublic = isPublic)
        }
    }

    fun updateSearch(searchText: String) {
        searchQuery.value = searchText
        _uiState.update { state ->
            state.copy(searchText = searchText)
        }
    }

    fun updateSpoiler(contentId: String, isSpoiler: Boolean) {
        _uiState.update { state ->
            val currentDetail = state.contentDetailsMap[contentId] ?: ContentDetail()
            val newDetailsMap = state.contentDetailsMap.toMutableMap().apply {
                put(contentId, currentDetail.copy(isSpoiler = isSpoiler))
            }
            state.copy(contentDetailsMap = newDetailsMap)
        }
    }

    fun updateReason(contentId: String, reason: String) {
        _uiState.update { state ->
            val currentDetail = state.contentDetailsMap[contentId] ?: ContentDetail()
            val newDetailsMap = state.contentDetailsMap.toMutableMap().apply {
                put(contentId, currentDetail.copy(reason = reason))
            }
            state.copy(contentDetailsMap = newDetailsMap)
        }
    }

    fun toggleContent(content: SearchContentItemModel) {
        _uiState.update { state ->
            val currentList = state.selectedContents
            val newList = if (currentList.any { it.id == content.id }) {
                val newDetailsMap = state.contentDetailsMap.toMutableMap().apply {
                    remove(content.id)
                }
                return@update state.copy(
                    selectedContents = currentList.filterNot { it.id == content.id }.toImmutableList(),
                    contentDetailsMap = newDetailsMap
                )
            } else {
                if (currentList.size < 10) {
                    currentList + content
                } else {
                    currentList
                }
            }
            state.copy(selectedContents = newList.toImmutableList())
        }
    }

    fun removeContent(content: SearchContentItemModel) {
        _uiState.update { state ->
            val newList = state.selectedContents.filterNot { it.id == content.id }
            val newDetailsMap = state.contentDetailsMap.toMutableMap().apply {
                remove(content.id)
            }
            state.copy(
                selectedContents = newList.toImmutableList(),
                contentDetailsMap = newDetailsMap
            )
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            searchQuery
                .debounce(400)
                .map { it.trim() }
                .distinctUntilChanged()
                .collectLatest { query ->
                    searchRepository.getSearchContentList(query)
                        .onSuccess { model ->
                            _uiState.update { it.copy(contents = model.contents) }
                        }
                        .onFailure {
                            _uiState.update { it.copy(contents = persistentListOf()) }
                        }
                }
        }
    }

    private fun loadInitialContents() {
        viewModelScope.launch {
            searchRepository.getSearchContentList("")
                .onSuccess { model ->
                    _uiState.update { it.copy(contents = model.contents) }
                }
                .onFailure {
                    _uiState.update { it.copy(contents = persistentListOf()) }
                }
        }
    }

    fun updateThumbnailImageUri(uri: Uri?) {
        _uiState.update { it.copy(thumbnailImageUri = uri) }
    }

    fun addContentImageUri(contentId: String, uri: Uri) {
        _uiState.update { state ->
            val current = state.contentDetailsMap[contentId] ?: ContentDetail()
            if (current.contentImageUris.size >= 5) return@update state
            val updated = current.copy(contentImageUris = current.contentImageUris + uri)
            state.copy(contentDetailsMap = state.contentDetailsMap + (contentId to updated))
        }
    }

    fun removeExistingContentImageUrl(contentId: String, index: Int) {
        _uiState.update { state ->
            val current = state.contentDetailsMap[contentId] ?: return@update state
            if (index !in current.existingImageUrls.indices) return@update state
            val updated = current.copy(
                existingImageUrls = current.existingImageUrls.toMutableList().also { it.removeAt(index) }
            )
            state.copy(contentDetailsMap = state.contentDetailsMap + (contentId to updated))
        }
    }

    fun removeContentImageUri(contentId: String, index: Int) {
        _uiState.update { state ->
            val current = state.contentDetailsMap[contentId] ?: return@update state
            if (index !in current.contentImageUris.indices) return@update state
            val updated = current.copy(
                contentImageUris = current.contentImageUris.toMutableList().also { it.removeAt(index) }
            )
            state.copy(contentDetailsMap = state.contentDetailsMap + (contentId to updated))
        }
    }

    private suspend fun uploadContentImagesIfNeeded(): Result<Map<String, List<String>>> {
        val result = mutableMapOf<String, List<String>>()
        for ((contentId, detail) in _uiState.value.contentDetailsMap) {
            val keys = mutableListOf<String>()
            for (uri in detail.contentImageUris) {
                val key = uploadImageIfNeeded(uri, StoragePathType.COLLECTION_CONTENT)
                    .getOrElse { return Result.failure(it) }
                if (key != null) keys.add(key)
            }
            if (keys.isNotEmpty()) result[contentId] = keys
        }
        return Result.success(result)
    }

    private suspend fun uploadImageIfNeeded(uri: Uri?, pathType: StoragePathType): Result<String?> {
        uri ?: return Result.success(null)

        val mimeType = runCatching {
            withContext(Dispatchers.IO) { context.contentResolver.getType(uri) }
        }.getOrElse { error ->
            Timber.e(error, "Failed to resolve mimeType")
            return Result.failure(error)
        } ?: "image/jpeg"
        val extension = mimeTypeToFileExtension(mimeType)

        val presignedUrl = storageRepository.getPresignedUrl(
            pathType = pathType,
            extension = extension,
        ).getOrElse { error ->
            Timber.e(error, "Failed to get presigned URL")
            return Result.failure(error)
        }

        val imageBytes = runCatching {
            withContext(Dispatchers.IO) {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            }
        }.getOrElse { error ->
            Timber.e(error, "Failed to read image bytes")
            return Result.failure(error)
        } ?: return Result.failure(IllegalStateException("Failed to open image stream: $uri"))

        storageRepository.uploadToS3(
            uploadUrl = presignedUrl.uploadUrl,
            imageBytes = imageBytes,
            mimeType = mimeType,
        ).getOrElse { error ->
            Timber.e(error, "Failed to upload image to S3")
            return Result.failure(error)
        }

        return Result.success(presignedUrl.key)
    }

    private fun mimeTypeToFileExtension(mimeType: String): FileExtension = when (mimeType) {
        "image/png" -> FileExtension.PNG
        "image/gif" -> FileExtension.GIF
        "image/webp" -> FileExtension.WEBP
        else -> FileExtension.JPEG
    }
}
