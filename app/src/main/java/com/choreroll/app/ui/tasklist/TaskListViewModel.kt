package com.choreroll.app.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choreroll.app.data.model.TaskEntity
import com.choreroll.app.data.model.TaskWithCategory
import com.choreroll.app.data.repository.CategoryRepository
import com.choreroll.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskListViewModel(
    private val taskRepository: TaskRepository,
    categoryRepository: CategoryRepository
) : ViewModel() {

    data class UiState(
        val tasks: List<TaskWithCategory> = emptyList(),
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            taskRepository.getAllWithCategory().collect { tasks ->
                _uiState.update { it.copy(tasks = tasks, isLoading = false) }
            }
        }
    }

    fun deleteTask(task: TaskWithCategory) {
        viewModelScope.launch {
            val entity = taskRepository.getById(task.id) ?: return@launch
            taskRepository.delete(entity)
        }
    }
}
