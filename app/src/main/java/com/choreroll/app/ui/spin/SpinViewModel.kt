package com.choreroll.app.ui.spin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choreroll.app.data.model.CategoryEntity
import com.choreroll.app.data.model.TaskEntity
import com.choreroll.app.data.repository.CategoryRepository
import com.choreroll.app.domain.usecase.CompleteTaskUseCase
import com.choreroll.app.domain.usecase.SpinResult
import com.choreroll.app.domain.usecase.SpinTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpinViewModel(
    private val spinTaskUseCase: SpinTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    categoryRepository: CategoryRepository
) : ViewModel() {

    data class UiState(
        val phase: SpinPhase = SpinPhase.Idle,
        val categories: List<CategoryEntity> = emptyList(),
        val selectedCategoryId: Long? = null,
        val currentTask: TaskEntity? = null,
        val currentTaskCategoryName: String? = null,
        val spinItems: List<TaskEntity> = emptyList(),
        val spinWinner: TaskEntity? = null,
        val message: String? = null
    )

    enum class SpinPhase { Idle, Spinning, Landed }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getAll().collect { cats ->
                _uiState.update { it.copy(categories = cats) }
            }
        }
    }

    fun selectCategory(categoryId: Long?) {
        _uiState.update { it.copy(selectedCategoryId = categoryId) }
    }

    fun spin(excludeTaskId: Long? = null) {
        viewModelScope.launch {
            val state = _uiState.value
            val result = spinTaskUseCase(
                categoryId = state.selectedCategoryId,
                excludeTaskId = excludeTaskId
            )
            when (result) {
                is SpinResult.NoTasks -> {
                    _uiState.update {
                        it.copy(
                            phase = SpinPhase.Idle,
                            message = "No tasks available!",
                            currentTask = null,
                            spinItems = emptyList(),
                            spinWinner = null
                        )
                    }
                }
                is SpinResult.SingleTask -> {
                    val catName = state.categories.find { c -> c.id == result.task.categoryId }?.name
                    _uiState.update {
                        it.copy(
                            phase = SpinPhase.Landed,
                            currentTask = result.task,
                            currentTaskCategoryName = catName,
                            spinItems = emptyList(),
                            spinWinner = null,
                            message = null
                        )
                    }
                }
                is SpinResult.Ready -> {
                    val catName = state.categories.find { c -> c.id == result.winner.categoryId }?.name
                    _uiState.update {
                        it.copy(
                            phase = SpinPhase.Spinning,
                            spinItems = result.allAvailable,
                            spinWinner = result.winner,
                            currentTask = result.winner,
                            currentTaskCategoryName = catName,
                            message = null
                        )
                    }
                }
            }
        }
    }

    fun onAnimationComplete() {
        _uiState.update { it.copy(phase = SpinPhase.Landed) }
    }

    fun completeCurrentTask() {
        val task = _uiState.value.currentTask ?: return
        viewModelScope.launch {
            completeTaskUseCase(task)
            _uiState.update {
                it.copy(
                    phase = SpinPhase.Idle,
                    currentTask = null,
                    spinItems = emptyList(),
                    spinWinner = null
                )
            }
        }
    }

    fun skip() {
        val currentId = _uiState.value.currentTask?.id
        spin(excludeTaskId = currentId)
    }

    fun reset() {
        _uiState.update {
            it.copy(
                phase = SpinPhase.Idle,
                currentTask = null,
                spinItems = emptyList(),
                spinWinner = null,
                message = null
            )
        }
    }
}
