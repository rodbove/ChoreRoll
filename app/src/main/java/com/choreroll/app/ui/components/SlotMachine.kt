package com.choreroll.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SlotMachine(
    state: SlotMachineState,
    modifier: Modifier = Modifier
) {
    val viewportHeight = SlotMachineState.ITEM_HEIGHT_DP * SlotMachineState.VISIBLE_ITEMS
    val shape = RoundedCornerShape(24.dp)
    val density = LocalDensity.current
    val itemHeightPx = with(density) { SlotMachineState.ITEM_HEIGHT_DP.toPx() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(viewportHeight)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                shape = shape
            )
    ) {
        // Virtual scrolling: only render items currently in/near viewport
        val scrollPx = state.scrollOffset.value
        val firstVisible = (scrollPx / itemHeightPx).toInt() - 1
        val lastVisible = firstVisible + SlotMachineState.VISIBLE_ITEMS + 2

        for (i in firstVisible..lastVisible) {
            if (i < 0 || i >= state.displayList.size) continue
            val task = state.displayList[i]
            val itemY = (i * itemHeightPx - scrollPx).roundToInt()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SlotMachineState.ITEM_HEIGHT_DP)
                    .offset { IntOffset(0, itemY) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 28.dp)
                )
            }
        }

        // Selection indicator — gold accent lines
        HorizontalDivider(
            modifier = Modifier.offset(y = SlotMachineState.ITEM_HEIGHT_DP),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.secondary
        )
        HorizontalDivider(
            modifier = Modifier.offset(y = SlotMachineState.ITEM_HEIGHT_DP * 2),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.secondary
        )

        // Fade edges
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SlotMachineState.ITEM_HEIGHT_DP)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0f)
                        )
                    )
                )
                .align(Alignment.TopCenter)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SlotMachineState.ITEM_HEIGHT_DP)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0f),
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
                .align(Alignment.BottomCenter)
        )
    }
}
