package com.choreroll.app.ui.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

private data class NavItem(
    val label: String,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val isSelected: Boolean,
    val onClick: () -> Unit
)

@Composable
fun ChoreRollNavGraph(
    container: AppContainer,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val tabs = listOf(
        NavItem(
            label = "Roll",
            icon = Icons.Outlined.Casino,
            iconSelected = Icons.Filled.Casino,
            isSelected = currentDestination?.hasRoute<SpinRoute>() == true,
            onClick = {
                navController.navigate(SpinRoute) {
                    popUpTo(SpinRoute) { inclusive = true }
                }
            }
        ),
        NavItem(
            label = "Tasks",
            icon = Icons.Outlined.TaskAlt,
            iconSelected = Icons.Filled.TaskAlt,
            isSelected = currentDestination?.hasRoute<TaskListRoute>() == true,
            onClick = {
                navController.navigate(TaskListRoute) { popUpTo(SpinRoute) }
            }
        ),
        NavItem(
            label = "History",
            icon = Icons.Outlined.History,
            iconSelected = Icons.Filled.History,
            isSelected = currentDestination?.hasRoute<HistoryRoute>() == true,
            onClick = {
                navController.navigate(HistoryRoute) { popUpTo(SpinRoute) }
            }
        ),
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = { CustomBottomBar(tabs) }
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
            composable<AddEditTaskRoute> { entry ->
                val route = entry.toRoute<AddEditTaskRoute>()
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
                    viewModel = viewModel { HistoryViewModel(container.completionDao) }
                )
            }
        }
    }
}

@Composable
private fun CustomBottomBar(tabs: List<NavItem>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                CustomNavItem(tab)
            }
        }
    }
}

@Composable
private fun CustomNavItem(item: NavItem) {
    val bgColor by animateColorAsState(
        targetValue = if (item.isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceContainer,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "navBg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (item.isSelected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "navContent"
    )
    val elevation by animateDpAsState(
        targetValue = if (item.isSelected) 2.dp else 0.dp,
        label = "navElevation"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(bgColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = item.onClick
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (item.isSelected) item.iconSelected else item.icon,
                contentDescription = item.label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            if (item.isSelected) {
                Text(
                    text = item.label,
                    color = contentColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}
