package com.choreroll.app.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object SpinRoute

@Serializable
object TaskListRoute

@Serializable
data class AddEditTaskRoute(val taskId: Long = -1L) // -1 means "add new"

@Serializable
object HistoryRoute
