package com.choreroll.app.domain.usecase

import com.choreroll.app.data.db.dao.CompletionDao
import com.choreroll.app.data.model.CompletionEntity
import com.choreroll.app.data.model.RecurrenceType
import com.choreroll.app.data.model.TaskEntity
import com.choreroll.app.data.repository.TaskRepository

class CompleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val completionDao: CompletionDao
) {
    suspend operator fun invoke(task: TaskEntity) {
        completionDao.insert(CompletionEntity(taskId = task.id))
        if (task.recurrenceType == RecurrenceType.ONE_TIME) {
            taskRepository.archiveTask(task)
        }
    }
}
