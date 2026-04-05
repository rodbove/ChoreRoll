package com.choreroll.app.ui.spin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.choreroll.app.ui.components.CategoryFilterRow
import com.choreroll.app.ui.components.SlotMachine
import com.choreroll.app.ui.components.SlotMachineState
import com.choreroll.app.ui.components.TaskCard

@Composable
fun SpinScreen(viewModel: SpinViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CategoryFilterRow(
            categories = uiState.categories,
            selectedId = uiState.selectedCategoryId,
            onSelect = viewModel::selectCategory
        )

        Spacer(Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (uiState.phase) {
                SpinViewModel.SpinPhase.Idle -> {
                    uiState.message?.let { msg ->
                        Text(
                            text = msg,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(24.dp))
                    }

                    Button(
                        onClick = { viewModel.spin() },
                        modifier = Modifier.size(180.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Casino,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Roll!",
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                }

                SpinViewModel.SpinPhase.Spinning -> {
                    val items = uiState.spinItems
                    val winner = uiState.spinWinner
                    if (items.isNotEmpty() && winner != null) {
                        val density = LocalDensity.current
                        val itemHeightPx = with(density) { SlotMachineState.ITEM_HEIGHT_DP.toPx() }
                        val slotState = remember(items, winner) {
                            SlotMachineState(items = items, winner = winner)
                        }

                        SlotMachine(
                            state = slotState,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        LaunchedEffect(slotState) {
                            slotState.spin(itemHeightPx)
                            viewModel.onAnimationComplete()
                        }
                    }
                }

                SpinViewModel.SpinPhase.Landed -> {
                    val task = uiState.currentTask
                    if (task != null) {
                        AnimatedVisibility(
                            visible = true,
                            enter = scaleIn(initialScale = 0.8f) + fadeIn()
                        ) {
                            TaskCard(
                                task = task,
                                categoryName = uiState.currentTaskCategoryName,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        Spacer(Modifier.height(32.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            OutlinedButton(onClick = { viewModel.skip() }) {
                                Text("Skip")
                            }
                            Button(onClick = { viewModel.completeCurrentTask() }) {
                                Text("Done!")
                            }
                        }
                    }
                }
            }
        }
    }
}
