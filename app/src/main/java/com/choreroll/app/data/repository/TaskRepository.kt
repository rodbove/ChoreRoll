package com.choreroll.app.data.repository

import com.choreroll.app.data.db.dao.TaskDao
import com.choreroll.app.data.model.TaskEntity
import com.choreroll.app.data.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllActiveWithCategory(): Flow<List<TaskWithCategory>> = taskDao.getAllActiveWithCategory()

    fun getAllWithCategory(): Flow<List<TaskWithCategory>> = taskDao.getAllWithCategory()

    suspend fun getById(id: Long): TaskEntity? = taskDao.getById(id)

    suspend fun insert(task: TaskEntity): Long = taskDao.insert(task)

    suspend fun update(task: TaskEntity) = taskDao.update(task)

    suspend fun delete(task: TaskEntity) = taskDao.delete(task)

    suspend fun getAvailableTasks(categoryId: Long? = null): List<TaskEntity> =
        taskDao.getAvailableTasks(categoryId)

    suspend fun archiveTask(task: TaskEntity) {
        taskDao.update(task.copy(isArchived = true))
    }
}
