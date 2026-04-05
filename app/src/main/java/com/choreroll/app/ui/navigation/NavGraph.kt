package com.choreroll.app.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.choreroll.app.di.AppContainer
import com.choreroll.app.ui.addedittask.AddEditTaskScreen
import com.choreroll.app.ui.addedittask.AddEditTaskViewModel
import com.choreroll.app.ui.history.HistoryScreen
import com.choreroll.app.ui.history.HistoryViewModel
import com.choreroll.app.ui.spin.SpinScreen
import com.choreroll.app.ui.spin.SpinViewModel
import com.choreroll.app.ui.tasklist.TaskListScreen
import com.choreroll.app.ui.tasklist.TaskListViewModel

@Composable
fun ChoreRollNavGraph(
    container: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                val navColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Casino, contentDescription = "Spin") },
                    label = { Text("Spin") },
                    selected = currentDestination?.hasRoute<SpinRoute>() == true,
                    colors = navColors,
                    onClick = {
                        navController.navigate(SpinRoute) {
                            popUpTo(SpinRoute) { inclusive = true }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Tasks") },
                    label = { Text("Tasks") },
                    selected = currentDestination?.hasRoute<TaskListRoute>() == true,
                    colors = navColors,
                    onClick = {
                        navController.navigate(TaskListRoute) {
                            popUpTo(SpinRoute)
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text("History") },
                    selected = currentDestination?.hasRoute<HistoryRoute>() == true,
                    colors = navColors,
                    onClick = {
                        navController.navigate(HistoryRoute) {
                            popUpTo(SpinRoute)
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = SpinRoute,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<SpinRoute> {
                SpinScreen(
                    viewModel = viewModel {
                        SpinViewModel(
                            container.spinTaskUseCase,
                            container.completeTaskUseCase,
                            container.categoryRepository
                        )
                    }
                )
            }
            composable<TaskListRoute> {
                TaskListScreen(
                    viewModel = viewModel {
                        TaskListViewModel(container.taskRepository, container.categoryRepository)
                    },
                    onAddTask = { navController.navigate(AddEditTaskRoute()) },
                    onEditTask = { taskId -> navController.navigate(AddEditTaskRoute(taskId)) }
                )
            }
            composable<AddEditTaskRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<AddEditTaskRoute>()
                val taskId = if (route.taskId == -1L) null else route.taskId
                AddEditTaskScreen(
                    viewModel = viewModel {
                        AddEditTaskViewModel(
                            container.taskRepository,
                            container.categoryRepository,
                            taskId
                        )
                    },
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable<HistoryRoute> {
                HistoryScreen(
                    viewModel = viewModel {
                        HistoryViewModel(container.completionDao)
                    }
                )
            }
        }
    }
}
