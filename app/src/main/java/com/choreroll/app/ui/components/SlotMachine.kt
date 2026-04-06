package com.choreroll.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun SlotMachine(
    state: SlotMachineState,
    modifier: Modifier = Modifier
) {
    val viewportHeight = SlotMachineState.ITEM_HEIGHT_DP * SlotMachineState.VISIBLE_ITEMS
    val shape = RoundedCornerShape(28.dp)
    val density = LocalDensity.current
    val itemHeightPx = with(density) { SlotMachineState.ITEM_HEIGHT_DP.toPx() }
    val viewportHeightPx = with(density) { viewportHeight.toPx() }
    val centerY = viewportHeightPx / 2f

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(viewportHeight)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .drawBehind {
                // Center highlight band
                val bandTop = (size.height - itemHeightPx) / 2f
                drawRect(
                    color = primaryColor.copy(alpha = 0.06f),
                    topLeft = Offset(0f, bandTop),
                    size = androidx.compose.ui.geometry.Size(size.width, itemHeightPx)
                )
                // Top line
                drawLine(
                    color = secondaryColor.copy(alpha = 0.5f),
                    start = Offset(24f, bandTop),
                    end = Offset(size.width - 24f, bandTop),
                    strokeWidth = 1.5f
                )
                // Bottom line
                drawLine(
                    color = secondaryColor.copy(alpha = 0.5f),
                    start = Offset(24f, bandTop + itemHeightPx),
                    end = Offset(size.width - 24f, bandTop + itemHeightPx),
                    strokeWidth = 1.5f
                )
            }
    ) {
        // Virtual scrolling: render only visible items
        val scrollPx = state.scrollOffset.value
        val firstVisible = (scrollPx / itemHeightPx).toInt() - 1
        val lastVisible = firstVisible + SlotMachineState.VISIBLE_ITEMS + 2

        for (i in firstVisible..lastVisible) {
            if (i < 0 || i >= state.displayList.size) continue
            val task = state.displayList[i]
            val itemY = (i * itemHeightPx - scrollPx).roundToInt()

            // Distance from center for opacity/scale effect
            val itemCenterY = itemY + itemHeightPx / 2f
            val distFromCenter = abs(itemCenterY - centerY) / centerY
            val alpha = (1f - distFromCenter * 0.6f).coerceIn(0.3f, 1f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SlotMachineState.ITEM_HEIGHT_DP)
                    .offset { IntOffset(0, itemY) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = task.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }

        // Fade edges
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SlotMachineState.ITEM_HEIGHT_DP)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surfaceContainer,
                            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0f)
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
                            MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0f),
                            MaterialTheme.colorScheme.surfaceContainer
                        )
                    )
                )
                .align(Alignment.BottomCenter)
        )
    }
}
