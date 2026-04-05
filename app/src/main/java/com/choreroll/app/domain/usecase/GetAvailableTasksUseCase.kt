package com.choreroll.app.domain.usecase

import com.choreroll.app.data.model.TaskEntity
import com.choreroll.app.data.repository.TaskRepository

class GetAvailableTasksUseCase(private val taskRepository: TaskRepository) {

    suspend operator fun invoke(categoryId: Long? = null): List<TaskEntity> {
        return taskRepository.getAvailableTasks(categoryId)
    }
}
