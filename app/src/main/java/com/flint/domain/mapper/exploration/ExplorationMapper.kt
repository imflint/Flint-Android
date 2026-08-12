package com.flint.domain.mapper.exploration

import com.flint.data.dto.exploration.response.ExplorationResponseDto
import com.flint.domain.model.exploration.ExplorationItemModel
import com.flint.domain.model.exploration.ExplorationSessionModel
import com.flint.domain.model.exploration.ExplorationState
import kotlinx.collections.immutable.toImmutableList
import timber.log.Timber

fun ExplorationResponseDto.toModel(): ExplorationSessionModel =
    ExplorationSessionModel(
        items = items.map { it.toModel() }.toImmutableList(),
        state = state.toExplorationState(),
        hasNext = hasNext,
    )

private fun String.toExplorationState(): ExplorationState =
    when (this) {
        "IN_PROGRESS" -> ExplorationState.IN_PROGRESS
        "END" -> ExplorationState.END
        "EMPTY" -> ExplorationState.EMPTY
        else -> {
            Timber.e("알 수 없는 탐색 state 값: $this")
            ExplorationState.UNKNOWN
        }
    }

private fun ExplorationResponseDto.Item.toModel(): ExplorationItemModel =
    ExplorationItemModel(
        contentId = contentId,
        title = title,
        description = description,
        imageUrl = imageUrl,
        year = year,
        collectionId = collectionId,
    )
