package com.choreroll.app.data.model

import androidx.room.ColumnInfo
import java.time.LocalDateTime

data class TaskWithCategory(
    val id: Long,
    val name: String,
    @ColumnInfo(name = "category_id") val categoryId: Long?,
    @ColumnInfo(name = "recurrence_type") val recurrenceType: RecurrenceType,
    @ColumnInfo(name = "interval_days") val intervalDays: Int?,
    @ColumnInfo(name = "is_archived") val isArchived: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime,
    @ColumnInfo(name = "category_name") val categoryName: String?
)
