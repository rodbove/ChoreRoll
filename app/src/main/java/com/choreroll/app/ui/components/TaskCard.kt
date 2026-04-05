package com.choreroll.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.choreroll.app.data.model.RecurrenceType
import com.choreroll.app.data.model.TaskEntity

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TaskCard(
    task: TaskEntity,
    categoryName: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                categoryName?.let { name ->
                    AssistChip(
                        onClick = {},
                        label = { Text(name) }
                    )
                }
                AssistChip(
                    onClick = {},
                    label = {
                        Text(
                            when (task.recurrenceType) {
                                RecurrenceType.ONE_TIME -> "One-time"
                                RecurrenceType.DAILY -> "Daily"
                                RecurrenceType.EVERY_N_DAYS -> "Every ${task.intervalDays} days"
                            }
                        )
                    }
                )
            }
        }
    }
}
