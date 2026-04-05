package com.choreroll.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.choreroll.app.data.db.dao.CategoryDao
import com.choreroll.app.data.db.dao.CompletionDao
import com.choreroll.app.data.db.dao.TaskDao
import com.choreroll.app.data.model.CategoryEntity
import com.choreroll.app.data.model.CompletionEntity
import com.choreroll.app.data.model.TaskEntity

@Database(
    entities = [TaskEntity::class, CategoryEntity::class, CompletionEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun completionDao(): CompletionDao
}
