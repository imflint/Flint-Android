package com.flint.presentation.collectioncreate

import androidx.lifecycle.ViewModel
import com.kakao.sdk.common.model.Description
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.lang.reflect.Constructor
import javax.inject.Inject

@HiltViewModel
class CollectionCreateViewModel @Inject constructor()
 : ViewModel() {
        private val _uiState =
            MutableStateFlow(
                CollectionCreateUiState()
            )

        val uiState: StateFlow<CollectionCreateUiState> =
            _uiState.asStateFlow()

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