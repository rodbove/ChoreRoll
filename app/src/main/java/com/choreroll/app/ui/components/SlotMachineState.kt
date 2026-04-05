package com.choreroll.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.dp
import com.choreroll.app.data.model.TaskEntity

class SlotMachineState(
    val items: List<TaskEntity>,
    val winner: TaskEntity
) {
    // Build a display list by repeating shuffled items, ending with the winner
    private val repetitions = maxOf(5, 30 / items.size + 1)

    val displayList: List<TaskEntity> = buildList {
        repeat(repetitions) {
            addAll(items.shuffled())
        }
        // Ensure the winner is the final item we land on
        add(winner)
    }

    val winnerIndex: Int = displayList.size - 1

    val scrollOffset = Animatable(0f)

    suspend fun spin(itemHeightPx: Float) {
        scrollOffset.snapTo(0f)

        // Target: scroll so winnerIndex lands in the center of the 3-item viewport
        val targetOffset = (winnerIndex - VISIBLE_ITEMS / 2) * itemHeightPx

        scrollOffset.animateTo(
            targetValue = targetOffset,
            animationSpec = tween(
                durationMillis = 3000,
                easing = SlotMachineEasing
            )
        )
    }

    companion object {
        val ITEM_HEIGHT_DP = 64.dp
        const val VISIBLE_ITEMS = 3

        // Fast start, very slow deceleration - feels like a slot machine winding down
        val SlotMachineEasing = CubicBezierEasing(0.15f, 0.0f, 0.05f, 1.0f)
    }
}
