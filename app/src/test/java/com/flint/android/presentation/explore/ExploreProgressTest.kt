package com.flint.android.presentation.explore

import com.flint.android.domain.model.exploration.ExplorationItemModel
import com.flint.android.domain.model.exploration.ExplorationSessionModel
import com.flint.android.domain.model.exploration.ExplorationState
import kotlinx.collections.immutable.toImmutableList
import org.junit.Assert.assertEquals
import org.junit.Test

class ExploreProgressTest {

    private fun item(position: Int) = ExplorationItemModel(
        contentId = "$position",
        title = "",
        description = "",
        imageUrl = "",
        year = 0,
        collectionId = "",
        position = position,
    )

    private fun session(
        positions: List<Int>,
        state: ExplorationState = ExplorationState.IN_PROGRESS,
        lastViewedPosition: Int = 0,
    ) = ExplorationSessionModel(
        items = positions.map(::item).toImmutableList(),
        state = state,
        lastViewedPosition = lastViewedPosition,
    )

    @Test
    fun `처음 보는 세션은 첫 작품부터 보여준다`() {
        assertEquals(0, session((1..30).toList()).resumePage())
    }

    @Test
    fun `진행 중이던 세션은 마지막으로 본 작품부터 보여준다`() {
        assertEquals(11, session((1..30).toList(), lastViewedPosition = 12).resumePage())
    }

    @Test
    fun `마지막으로 본 작품이 숨김 처리됐으면 바로 앞 작품부터 보여준다`() {
        val positions = (1..30).filter { it != 12 }

        assertEquals(10, session(positions, lastViewedPosition = 12).resumePage())
    }

    @Test
    fun `END 세션은 맨 위로 돌리지 않고 End 페이지를 보여준다`() {
        val positions = (1..30).toList()

        assertEquals(30, session(positions, ExplorationState.END, lastViewedPosition = 30).resumePage())
    }

    @Test
    fun `저장이 밀려 있으면 빠진 위치부터 순서대로 보낸다`() {
        val items = (1..30).map(::item)

        assertEquals(listOf(6, 7, 8), items.positionsToSave(saved = 5, target = 8))
    }

    @Test
    fun `숨김 처리돼 빠진 위치는 건너뛴다`() {
        val items = (1..30).filter { it != 7 }.map(::item)

        assertEquals(listOf(6, 8), items.positionsToSave(saved = 5, target = 8))
    }

    @Test
    fun `이미 저장한 위치로 되돌아가면 보낼 것이 없다`() {
        val items = (1..30).map(::item)

        assertEquals(emptyList<Int>(), items.positionsToSave(saved = 10, target = 4))
    }
}
