package com.choreroll.app.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.choreroll.app.data.model.CompletionEntity
import com.choreroll.app.data.model.CompletionWithTask
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface CompletionDao {
    @Insert
    suspend fun insert(completion: CompletionEntity): Long

    @Query("""
        SELECT c.id, c.task_id, c.completed_at, t.name as task_name, t.category_id, cat.name as category_name
        FROM completions c
        INNER JOIN tasks t ON c.task_id = t.id
        LEFT JOIN categories cat ON t.category_id = cat.id
        ORDER BY c.completed_at DESC
    """)
    fun getAllWithTaskInfo(): Flow<List<CompletionWithTask>>

    @Query("SELECT MAX(completed_at) FROM completions WHERE task_id = :taskId")
    suspend fun getLastCompletionDate(taskId: Long): LocalDateTime?
}
