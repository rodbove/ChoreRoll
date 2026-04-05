package com.choreroll.app.data.db

import androidx.room.TypeConverter
import com.choreroll.app.data.model.RecurrenceType
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): String? = value?.toString()

    @TypeConverter
    fun toLocalDateTime(value: String?): LocalDateTime? = value?.let { LocalDateTime.parse(it) }

    @TypeConverter
    fun fromRecurrenceType(value: RecurrenceType): String = value.name

    @TypeConverter
    fun toRecurrenceType(value: String): RecurrenceType = RecurrenceType.valueOf(value)
}
