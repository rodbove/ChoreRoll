package com.choreroll.app.widget

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.choreroll.app.data.db.DatabaseProvider
import com.choreroll.app.data.repository.TaskRepository
import com.choreroll.app.domain.usecase.GetAvailableTasksUseCase
import com.choreroll.app.domain.usecase.SpinResult
import com.choreroll.app.domain.usecase.SpinTaskUseCase

class RollActionCallback : ActionCallback {
    companion object {
        val TASK_NAME_KEY = stringPreferencesKey("rolled_task_name")
        val NO_TASKS_KEY = booleanPreferencesKey("no_tasks")
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val db = DatabaseProvider.getDatabase(context)
        val taskRepo = TaskRepository(db.taskDao())
        val getAvailable = GetAvailableTasksUseCase(taskRepo)
        val spin = SpinTaskUseCase(getAvailable)

        val result = spin()

        updateAppWidgetState(context, glanceId) { prefs ->
            when (result) {
                is SpinResult.NoTasks -> {
                    prefs[TASK_NAME_KEY] = ""
                    prefs[NO_TASKS_KEY] = true
                }
                is SpinResult.SingleTask -> {
                    prefs[TASK_NAME_KEY] = result.task.name
                    prefs[NO_TASKS_KEY] = false
                }
                is SpinResult.Ready -> {
                    prefs[TASK_NAME_KEY] = result.winner.name
                    prefs[NO_TASKS_KEY] = false
                }
            }
        }

        ChoreRollWidget().update(context, glanceId)
    }
}
