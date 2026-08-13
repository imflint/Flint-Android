package com.flint.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.core.common.util.UiState
import com.flint.domain.model.exploration.ExplorationItemModel
import com.flint.domain.model.exploration.ExplorationSessionModel
import com.flint.domain.repository.ExplorationRepository
import com.flint.presentation.explore.uistate.ExploreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val explorationRepository: ExplorationRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<ExploreUiState>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<ExploreUiState>> = _uiState.asStateFlow()

    init {
        loadSession()
    }

    fun retry() {
        _uiState.update { UiState.Loading }
        loadSession()
    }

    private fun loadSession() {
        viewModelScope.launch {
            explorationRepository.getExplorationSession()
                .onSuccess { session -> onSessionLoaded(session) }
                .onFailure { error ->
                    _uiState.update { UiState.Failure }
                    Timber.e(error, "탐색 세션 조회 실패")
                }
        }
    }

    private suspend fun onSessionLoaded(session: ExplorationSessionModel) {
        if (session.isEnd && session.hasNext) {
            fetchNextSession(previousItems = persistentListOf())
            return
        }

        applySession(session, previousItems = persistentListOf())
    }

    fun advanceToNextSession() {
        val currentState = (_uiState.value as? UiState.Success)?.data ?: return
        if (!currentState.canAdvance) return

        _uiState.update { UiState.Success(currentState.copy(isLoadingNext = true)) }

        viewModelScope.launch {
            fetchNextSession(previousItems = currentState.items)
        }
    }

    private suspend fun fetchNextSession(previousItems: ImmutableList<ExplorationItemModel>) {
        explorationRepository.advanceToNextExplorationSession()
            .onSuccess { session -> applySession(session, previousItems = previousItems) }
            .onFailure { error ->
                _uiState.update { current ->
                    when (current) {
                        is UiState.Success -> UiState.Success(current.data.copy(isLoadingNext = false))
                        else -> UiState.Failure
                    }
                }
                Timber.e(error, "다음 탐색 세션 조회 실패")
            }
    }

    private fun applySession(
        session: ExplorationSessionModel,
        previousItems: ImmutableList<ExplorationItemModel>,
    ) {
        val mergedItems = if (session.isEnd && previousItems.isNotEmpty()) {
            previousItems
        } else {
            (previousItems + session.items).toImmutableList()
        }

        _uiState.update {
            UiState.Success(
                ExploreUiState(
                    items = mergedItems,
                    state = session.state,
                    hasNext = session.hasNext,
                    isLoadingNext = false,
                    initialPage = if (session.isEnd) mergedItems.size else 0,
                )
            )
        }
    }
}
