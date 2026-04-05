package com.choreroll.app.ui.addedittask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choreroll.app.data.model.CategoryEntity
import com.choreroll.app.data.model.RecurrenceType
import com.choreroll.app.data.model.TaskEntity
import com.choreroll.app.data.repository.CategoryRepository
import com.choreroll.app.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddEditTaskViewModel(
    private val taskRepository: TaskRepository,
    private val categoryRepository: CategoryRepository,
    private val taskId: Long?
) : ViewModel() {

    data class UiState(
        val isEdit: Boolean = false,
        val name: String = "",
        val categoryText: String = "",
        val recurrenceType: RecurrenceType = RecurrenceType.ONE_TIME,
        val intervalDays: String = "7",
        val existingCategories: List<CategoryEntity> = emptyList(),
        val nameError: String? = null,
        val intervalError: String? = null,
        val isSaving: Boolean = false,
        val savedSuccessfully: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            categoryRepository.getAll().collect { cats ->
                _uiState.update { it.copy(existingCategories = cats) }
            }
        }
        if (taskId != null) {
            viewModelScope.launch {
                val task = taskRepository.getById(taskId) ?: return@launch
                val categories = _uiState.value.existingCategories
                val catName = task.categoryId?.let { catId ->
                    categories.find { it.id == catId }?.name ?: ""
                } ?: ""
                _uiState.update {
                    it.copy(
                        isEdit = true,
                        name = task.name,
                        categoryText = catName,
                        recurrenceType = task.recurrenceType,
                        intervalDays = task.intervalDays?.toString() ?: "7"
                    )
                }
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name, nameError = null) }
    }

    fun updateCategoryText(text: String) {
        _uiState.update { it.copy(categoryText = text) }
    }

    fun updateRecurrenceType(type: RecurrenceType) {
        _uiState.update { it.copy(recurrenceType = type) }
    }

    fun updateIntervalDays(days: String) {
        _uiState.update { it.copy(intervalDays = days, intervalError = null) }
    }

    fun save() {
        val state = _uiState.value

        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Name is required") }
            return
        }

        if (state.recurrenceType == RecurrenceType.EVERY_N_DAYS) {
            val interval = state.intervalDays.toIntOrNull()
            if (interval == null || interval < 1) {
                _uiState.update { it.copy(intervalError = "Enter a positive number") }
                return
            }
        }

        _uiState.update { it.copy(isSaving = true) }

        viewModelScope.launch {
            val categoryId = if (state.categoryText.isBlank()) null
            else categoryRepository.getOrCreate(state.categoryText)

            val intervalDays = if (state.recurrenceType == RecurrenceType.EVERY_N_DAYS)
                state.intervalDays.toIntOrNull()
            else null

            if (state.isEdit && taskId != null) {
                val existing = taskRepository.getById(taskId) ?: return@launch
                taskRepository.update(
                    existing.copy(
                        name = state.name.trim(),
                        categoryId = categoryId,
                        recurrenceType = state.recurrenceType,
                        intervalDays = intervalDays
                    )
                )
            } else {
                taskRepository.insert(
                    TaskEntity(
                        name = state.name.trim(),
                        categoryId = categoryId,
                        recurrenceType = state.recurrenceType,
                        intervalDays = intervalDays
                    )
                )
            }

            _uiState.update { it.copy(isSaving = false, savedSuccessfully = true) }
        }
    }
}
