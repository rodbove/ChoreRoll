package com.choreroll.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SlotMachine(
    state: SlotMachineState,
    modifier: Modifier = Modifier
) {
    val viewportHeight = SlotMachineState.ITEM_HEIGHT_DP * SlotMachineState.VISIBLE_ITEMS

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(viewportHeight)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // The scrolling column of task names
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = -state.scrollOffset.value
                }
        ) {
            state.displayList.forEach { task ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SlotMachineState.ITEM_HEIGHT_DP),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = task.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }
        }

        // Selection indicator lines - mark the center "winning" slot
        HorizontalDivider(
            modifier = Modifier.offset(y = SlotMachineState.ITEM_HEIGHT_DP),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )
        HorizontalDivider(
            modifier = Modifier.offset(y = SlotMachineState.ITEM_HEIGHT_DP * 2),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primary
        )

        // Fade edges at top and bottom for a polished look
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(SlotMachineState.ITEM_HEIGHT_DP)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
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
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
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
