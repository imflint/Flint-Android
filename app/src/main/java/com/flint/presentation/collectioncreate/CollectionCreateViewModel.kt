package com.flint.presentation.collectioncreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.data.dto.collection.request.CollectionCreateRequestDto
import com.flint.domain.mapper.collection.toDto
import com.flint.domain.model.collection.CollectionCreateContentModel
import com.flint.domain.model.collection.CollectionCreateRequestModel
import com.flint.domain.model.search.SearchContentItemModel
import com.flint.domain.model.search.SearchContentListModel
import com.flint.domain.repository.CollectionRepository
import com.flint.domain.repository.SearchRepository
import com.kakao.sdk.common.model.Description
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

import javax.inject.Inject

@HiltViewModel
class CollectionCreateViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val searchRepository: SearchRepository
)
 : ViewModel() {
    private val _uiState = MutableStateFlow(CollectionCreateUiState())
    val uiState: StateFlow<CollectionCreateUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")

    private val _createSuccess = MutableStateFlow<UiState<String>>(UiState.Loading)
    val createSuccess = _createSuccess.asStateFlow()


    init {
        observeSearchQuery()
        loadInitialContents()
    }

    fun onClickFinish() {
        postCollectionCreate()
    }

    private fun postCollectionCreate() {
        viewModelScope.launch {
            val requestModel = CollectionCreateRequestModel(
                imageUrl = "",
                title = uiState.value.title,
                description = uiState.value.description.ifBlank { "" },
                isPublic = uiState.value.isPublic ?: true,
                contentList = uiState.value.selectedContents.map { content ->
                    val detail = uiState.value.contentDetailsMap[content.id] ?: ContentDetail()
                    CollectionCreateContentModel(
                        contentId = content.id,
                        isSpoiler = detail.isSpoiler,
                        reason = detail.reason.ifBlank { "" },
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
}
