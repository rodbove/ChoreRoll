package com.choreroll.app.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.choreroll.app.data.db.dao.CompletionDao
import com.choreroll.app.data.model.CompletionWithTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class HistoryViewModel(
    completionDao: CompletionDao
) : ViewModel() {

    data class UiState(
        val completionsByDate: Map<LocalDate, List<CompletionWithTask>> = emptyMap(),
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            completionDao.getAllWithTaskInfo().collect { completions ->
                val grouped = completions.groupBy { it.completedAt.toLocalDate() }
                _uiState.update { it.copy(completionsByDate = grouped, isLoading = false) }
            }
        }
    }
}
