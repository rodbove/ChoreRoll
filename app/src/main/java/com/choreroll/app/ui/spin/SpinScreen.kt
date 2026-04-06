package com.choreroll.app.ui.spin

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.choreroll.app.data.model.RecurrenceType
import com.choreroll.app.ui.components.CategoryFilterRow
import com.choreroll.app.ui.components.SlotMachine
import com.choreroll.app.ui.components.SlotMachineState

@Composable
fun SpinScreen(viewModel: SpinViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        CategoryFilterRow(
            categories = uiState.categories,
            selectedId = uiState.selectedCategoryId,
            onSelect = viewModel::selectCategory
        )

        Spacer(Modifier.height(8.dp))

        // Main content
        AnimatedContent(
            targetState = uiState.phase,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            transitionSpec = {
                (fadeIn(tween(300)) + slideInVertically { it / 8 })
                    .togetherWith(fadeOut(tween(200)))
            },
            label = "phase"
        ) { phase ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (phase) {
                    SpinViewModel.SpinPhase.Idle -> IdleContent(
                        message = uiState.message,
                        onSpin = { viewModel.spin() }
                    )
                    SpinViewModel.SpinPhase.Spinning -> SpinningContent(
                        items = uiState.spinItems,
                        winner = uiState.spinWinner,
                        onComplete = { viewModel.onAnimationComplete() }
                    )
                    SpinViewModel.SpinPhase.Landed -> LandedContent(
                        taskName = uiState.currentTask?.name ?: "",
                        categoryName = uiState.currentTaskCategoryName,
                        recurrenceLabel = uiState.currentTask?.let {
                            when (it.recurrenceType) {
                                RecurrenceType.ONE_TIME -> "one-time"
                                RecurrenceType.DAILY -> "daily"
                                RecurrenceType.EVERY_N_DAYS -> "every ${it.intervalDays}d"
                            }
                        },
                        onSkip = { viewModel.skip() },
                        onComplete = { viewModel.completeCurrentTask() }
                    )
                }
            }
        }
    }
}

@Composable
private fun IdleContent(message: String?, onSpin: () -> Unit) {
    val pulse = rememberInfiniteTransition(label = "pulse")
    val scale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val glowAlpha by pulse.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    message?.let { msg ->
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 20.dp, vertical = 10.dp)
        ) {
            Text(
                msg,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(40.dp))
    }

    // Glow
    Box(
        modifier = Modifier
            .size(220.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = glowAlpha),
                        MaterialTheme.colorScheme.primary.copy(alpha = 0f)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onSpin
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "ROLL",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimary,
                    letterSpacing = 4.sp
                )
            }
        }
    }

    Spacer(Modifier.height(32.dp))
    Text(
        "tap to get a task",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun SpinningContent(
    items: List<com.choreroll.app.data.model.TaskEntity>,
    winner: com.choreroll.app.data.model.TaskEntity?,
    onComplete: () -> Unit
) {
    if (items.isNotEmpty() && winner != null) {
        val density = LocalDensity.current
        val itemHeightPx = with(density) { SlotMachineState.ITEM_HEIGHT_DP.toPx() }
        val slotState = remember(items, winner) {
            SlotMachineState(items = items, winner = winner)
        }

        SlotMachine(state = slotState)

        LaunchedEffect(slotState) {
            slotState.spin(itemHeightPx)
            onComplete()
        }
    }
}

@Composable
private fun LandedContent(
    taskName: String,
    categoryName: String?,
    recurrenceLabel: String?,
    onSkip: () -> Unit,
    onComplete: () -> Unit
) {
    // Task name big and bold
    Text(
        text = taskName,
        style = MaterialTheme.typography.displayLarge,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(Modifier.height(12.dp))

    // Tags row
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        categoryName?.let {
            TagPill(it, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
        }
        recurrenceLabel?.let {
            TagPill(
                it,
                MaterialTheme.colorScheme.surfaceVariant,
                MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    Spacer(Modifier.height(48.dp))

    // Action buttons
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Skip — outlined circle
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(
                    2.dp,
                    MaterialTheme.colorScheme.outline,
                    CircleShape
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onSkip
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = "Skip",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
        }

        // Done — filled pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(28.dp))
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onComplete
                )
                .padding(horizontal = 36.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    "Done",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun TagPill(
    text: String,
    bg: androidx.compose.ui.graphics.Color,
    fg: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = fg,
            letterSpacing = 0.3.sp
        )
    }
}
