package com.choreroll.app.ui.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.choreroll.app.data.model.RecurrenceType
import com.choreroll.app.data.model.TaskWithCategory
import com.choreroll.app.ui.components.EmptyState

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    onAddTask: () -> Unit,
    onEditTask: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTask) {
                Icon(Icons.Default.Add, contentDescription = "Add task")
            }
        }
    ) { paddingValues ->
        if (uiState.tasks.isEmpty() && !uiState.isLoading) {
            EmptyState(
                icon = Icons.AutoMirrored.Filled.ListAlt,
                title = "No tasks yet",
                subtitle = "Tap + to add your first task",
                modifier = Modifier.padding(paddingValues)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
            ) {
                items(uiState.tasks, key = { it.id }) { task ->
                    TaskListItem(
                        task = task,
                        onClick = { onEditTask(task.id) },
                        onDelete = { viewModel.deleteTask(task) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TaskListItem(
    task: TaskWithCategory,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isArchived)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isArchived) TextDecoration.LineThrough else null,
                    color = if (task.isArchived)
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    task.categoryName?.let { name ->
                        AssistChip(
                            onClick = {},
                            label = { Text(name, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                when (task.recurrenceType) {
                                    RecurrenceType.ONE_TIME -> if (task.isArchived) "Completed" else "One-time"
                                    RecurrenceType.DAILY -> "Daily"
                                    RecurrenceType.EVERY_N_DAYS -> "Every ${task.intervalDays}d"
                                },
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
