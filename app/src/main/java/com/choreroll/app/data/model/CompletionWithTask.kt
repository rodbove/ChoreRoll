package com.choreroll.app.data.model

import androidx.room.ColumnInfo
import java.time.LocalDateTime

data class CompletionWithTask(
    val id: Long,
    @ColumnInfo(name = "task_id") val taskId: Long,
    @ColumnInfo(name = "completed_at") val completedAt: LocalDateTime,
    @ColumnInfo(name = "task_name") val taskName: String,
    @ColumnInfo(name = "category_id") val categoryId: Long?,
    @ColumnInfo(name = "category_name") val categoryName: String?
)
