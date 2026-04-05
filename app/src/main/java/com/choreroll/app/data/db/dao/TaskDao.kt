package com.choreroll.app.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.choreroll.app.data.model.TaskEntity
import com.choreroll.app.data.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("""
        SELECT t.*, cat.name as category_name
        FROM tasks t
        LEFT JOIN categories cat ON t.category_id = cat.id
        WHERE t.is_archived = 0
        ORDER BY t.name ASC
    """)
    fun getAllActiveWithCategory(): Flow<List<TaskWithCategory>>

    @Query("""
        SELECT t.*, cat.name as category_name
        FROM tasks t
        LEFT JOIN categories cat ON t.category_id = cat.id
        ORDER BY t.is_archived ASC, t.name ASC
    """)
    fun getAllWithCategory(): Flow<List<TaskWithCategory>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Long): TaskEntity?

    @Insert
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("""
        SELECT t.* FROM tasks t
        WHERE t.is_archived = 0
        AND (
            (t.recurrence_type = 'ONE_TIME' AND NOT EXISTS (
                SELECT 1 FROM completions c WHERE c.task_id = t.id
            ))
            OR
            (t.recurrence_type = 'DAILY' AND NOT EXISTS (
                SELECT 1 FROM completions c
                WHERE c.task_id = t.id
                AND date(c.completed_at) = date('now', 'localtime')
            ))
            OR
            (t.recurrence_type = 'EVERY_N_DAYS' AND (
                NOT EXISTS (
                    SELECT 1 FROM completions c WHERE c.task_id = t.id
                )
                OR (
                    julianday('now', 'localtime') - julianday((
                        SELECT MAX(c.completed_at) FROM completions c WHERE c.task_id = t.id
                    )) >= t.interval_days
                )
            ))
        )
        AND (:categoryId IS NULL OR t.category_id = :categoryId)
    """)
    suspend fun getAvailableTasks(categoryId: Long? = null): List<TaskEntity>
}
