package com.flint.android.presentation.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flint.android.core.common.util.UiState
import com.flint.android.domain.model.exploration.ExplorationItemModel
import com.flint.android.domain.model.exploration.ExplorationSessionModel
import com.flint.android.domain.model.exploration.ExplorationState
import com.flint.android.domain.repository.ExplorationRepository
import com.flint.android.presentation.explore.uistate.ExploreUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val explorationRepository: ExplorationRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<ExploreUiState>> =
        MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<ExploreUiState>> = _uiState.asStateFlow()

    // 서버가 저장을 확인한 위치와 사용자가 내려가 본 가장 먼 위치.
    // 서버는 위치 회귀·건너뛰기를 409로 거부하므로, 둘 사이를 순서대로 한 칸씩 저장한다.
    private var savedPosition: Int = 0
    private var furthestPosition: Int = 0
    private val progressMutex = Mutex()

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
                .onSuccess { session -> applySession(session) }
                .onFailure { error ->
                    _uiState.update { UiState.Failure }
                    Timber.e(error, "탐색 세션 조회 실패")
                }
        }
    }

    private fun applySession(session: ExplorationSessionModel) {
        if (session.state == ExplorationState.UNKNOWN) {
            _uiState.update { UiState.Failure }
            Timber.e("알 수 없는 탐색 state로 세션을 적용할 수 없어 에러 화면으로 대체합니다.")
            return
        }

        savedPosition = session.lastViewedPosition
        furthestPosition = session.lastViewedPosition

        _uiState.update {
            UiState.Success(
                ExploreUiState(
                    items = session.items,
                    state = session.state,
                    initialPage = session.resumePage(),
                ),
            )
        }
    }

    fun onPageSettled(page: Int) {
        val data = (_uiState.value as? UiState.Success)?.data ?: return
        if (data.isEnd) return

        data.items.getOrNull(page)?.let { item ->
            furthestPosition = maxOf(furthestPosition, item.position)
        }
        // 위로 되돌아간 경우에도 앞선 저장이 실패해 밀려 있으면 다시 시도한다.
        if (savedPosition >= furthestPosition) return

        viewModelScope.launch { saveProgress(data.items) }
    }

    private suspend fun saveProgress(items: List<ExplorationItemModel>) {
        progressMutex.withLock {
            for (position in items.positionsToSave(saved = savedPosition, target = furthestPosition)) {
                val session = explorationRepository.updateExplorationProgress(position)
                    .onFailure { error -> Timber.e(error, "탐색 진행 위치 저장 실패: $position") }
                    .getOrNull() ?: return@withLock

                savedPosition = session.lastViewedPosition
                if (session.isEnd) showEndPage()
            }
        }
    }

    // 마지막 작품까지 저장해 서버가 END를 내려주면 그 아래에 End 페이지를 붙인다.
    private fun showEndPage() {
        _uiState.update { current ->
            if (current is UiState.Success) {
                UiState.Success(current.data.copy(state = ExplorationState.END))
            } else {
                current
            }
        }
    }
}

/**
 * 재진입 시 이어서 보여줄 페이지.
 * END면 End 페이지(맨 위로 돌리지 않음), 진행 중이면 마지막으로 본 작품.
 * 그 작품이 숨김·삭제로 빠졌으면 바로 앞 작품으로 맞춘다.
 */
internal fun ExplorationSessionModel.resumePage(): Int =
    if (isEnd) {
        items.size
    } else {
        items.indexOfLast { it.position <= lastViewedPosition }.coerceAtLeast(0)
    }

/** [saved] 다음부터 [target]까지, 세션에 남아 있는 작품의 위치를 순서대로 */
internal fun List<ExplorationItemModel>.positionsToSave(saved: Int, target: Int): List<Int> =
    map { it.position }
        .filter { it in (saved + 1)..target }
        .sorted()
