package com.choreroll.app.di

import android.content.Context
import com.choreroll.app.data.db.AppDatabase
import com.choreroll.app.data.db.DatabaseProvider
import com.choreroll.app.data.db.dao.CompletionDao
import com.choreroll.app.data.repository.CategoryRepository
import com.choreroll.app.data.repository.TaskRepository
import com.choreroll.app.domain.usecase.CompleteTaskUseCase
import com.choreroll.app.domain.usecase.GetAvailableTasksUseCase
import com.choreroll.app.domain.usecase.SpinTaskUseCase

class AppContainer(context: Context) {

    private val database: AppDatabase by lazy {
        DatabaseProvider.getDatabase(context)
    }

    val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(database.categoryDao())
    }

    val completionDao: CompletionDao by lazy {
        database.completionDao()
    }

    val getAvailableTasksUseCase: GetAvailableTasksUseCase by lazy {
        GetAvailableTasksUseCase(taskRepository)
    }

    val completeTaskUseCase: CompleteTaskUseCase by lazy {
        CompleteTaskUseCase(taskRepository, completionDao)
    }

    val spinTaskUseCase: SpinTaskUseCase by lazy {
        SpinTaskUseCase(getAvailableTasksUseCase)
    }
}
