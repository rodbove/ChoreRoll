package com.choreroll.app.domain.usecase

import com.choreroll.app.data.model.TaskEntity

class SpinTaskUseCase(
    private val getAvailableTasks: GetAvailableTasksUseCase
) {
    suspend operator fun invoke(
        categoryId: Long? = null,
        excludeTaskId: Long? = null
    ): SpinResult {
        val available = getAvailableTasks(categoryId).let { tasks ->
            if (excludeTaskId != null) tasks.filter { it.id != excludeTaskId }
            else tasks
        }

        return when {
            available.isEmpty() -> SpinResult.NoTasks
            available.size == 1 -> SpinResult.SingleTask(available.first())
            else -> {
                val winner = available.random()
                SpinResult.Ready(winner = winner, allAvailable = available)
            }
        }
    }
}

sealed class SpinResult {
    data object NoTasks : SpinResult()
    data class SingleTask(val task: TaskEntity) : SpinResult()
    data class Ready(val winner: TaskEntity, val allAvailable: List<TaskEntity>) : SpinResult()
}
