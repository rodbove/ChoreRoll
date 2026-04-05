package com.choreroll.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SlotMachine(
    state: SlotMachineState,
    modifier: Modifier = Modifier
) {
    val viewportHeight = SlotMachineState.ITEM_HEIGHT_DP * SlotMachineState.VISIBLE_ITEMS
    val shape = RoundedCornerShape(24.dp)

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
        // Scrolling column
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
