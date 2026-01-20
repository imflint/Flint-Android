package com.flint.presentation.collectioncreate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.data.dto.collection.request.CollectionCreateRequestDto
import com.flint.domain.mapper.collection.toDto
import com.flint.domain.model.collection.CollectionCreateContentModel
import com.flint.domain.model.collection.CollectionCreateRequestModel
import com.flint.domain.repository.CollectionRepository
import com.kakao.sdk.common.model.Description
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.flint.presentation.collectioncreate.model.CollectionContentUiModel

import javax.inject.Inject

@HiltViewModel
class CollectionCreateViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
)
 : ViewModel() {
    private val _uiState = MutableStateFlow(CollectionCreateUiState())
    val uiState: StateFlow<CollectionCreateUiState> = _uiState.asStateFlow()

    fun onClickFinish() {
        postCollectionCreate()
    }

    private fun postCollectionCreate() {
        viewModelScope.launch {
            val requestModel =
                CollectionCreateRequestModel(
                    imageUrl = "",
                    title = uiState.value.title.ifBlank { "더미 컬렉션 제목" },
                    description = uiState.value.description.ifBlank { "더미 설명" },
                    isPublic = uiState.value.isPublic ?: true,
                    contentList =
                        CollectionContentUiModel.dummyContentList.map { uiModel ->
                            CollectionCreateContentModel(
                                contentId = uiModel.contentId,
                                isSpoiler = false,
                                reason = "더미 이유",
                            )
                        },
                )

            collectionRepository
                .postCollectionCreate(requestModel.toDto())
                .onSuccess { println("컬렉션 생성 성공") }
                .onFailure { e -> println("컬렉션 생성 실패: ${e.message}") }
        }
    }


    fun updateTitle(title: String) {
        _uiState.update { state ->
            state.copy(title = title)
        }
    }

    fun updateDescription(description: String) {
        _uiState.update { state ->
            // description만 교체
            state.copy(description = description)
        }
    }

    fun updateIsPublic(isPublic: Boolean?) {
        _uiState.update { state ->
            state.copy(isPublic = isPublic)
        }
    }

//        fun addSelectedContent(content: CollectionContentUiModel) {
//            _uiState.update { state ->
//                val tempList: MutableList<CollectionContentUiModel> =
//                    mutableListOf(content)
//
//                tempList.addAll(state.selectedContents)
//
//                state.copy(
//                    selectedContents = tempList
//                )
//            }
//        }
//
//        fun removeSelectedContent(content: CollectionContentUiModel) {
//            _uiState.update { state ->
//                val tempList: MutableList<CollectionContentUiModel> =
//                    mutableListOf(content)
//
//                tempList.removeAll (state.selectedContents)
//
//                state.copy(
//                    selectedContents = tempList
//                )
//            }
//        }

//        fun updateIsFinishButton(isFinishedButtonEnabled: Boolean){
//            _uiState.update { state ->
//                if (uiState.selectedContents.size >= 2){
//                    state.copy(isFinishButtonEnabled = true)
//                }
//
//            }
//        }
    }
