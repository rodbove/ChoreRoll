package com.choreroll.app.widget

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.choreroll.app.MainActivity

class ChoreRollWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val prefs = currentState<Preferences>()
            val taskName = prefs[RollActionCallback.TASK_NAME_KEY]
            val noTasks = prefs[RollActionCallback.NO_TASKS_KEY] ?: false

            GlanceTheme {
                WidgetContent(taskName = taskName, noTasks = noTasks)
            }
        }
    }
}

@androidx.compose.runtime.Composable
private fun WidgetContent(taskName: String?, noTasks: Boolean) {
    val pink = Color(0xFFE91E6B)
    val pinkDark = Color(0xFFC4175A)
    val gold = Color(0xFFFFD700)
    val white = Color(0xFFFFFFFF)
    val cream = Color(0xFFFAF7F4)
    val darkBg = Color(0xFF1E1E2A)
    val darkCard = Color(0xFF252535)

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .cornerRadius(24)
            .background(ColorProvider(day = cream, night = darkBg))
            .padding(16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Row(
            modifier = GlanceModifier
                .fillMaxWidth()
                .clickable(actionStartActivity<MainActivity>()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "\uD83C\uDFB2",
                style = TextStyle(fontSize = androidx.compose.ui.unit.TextUnit.Unspecified)
            )
            Spacer(modifier = GlanceModifier.width(6))
            Text(
                text = "ChoreRoll",
                style = TextStyle(
                    color = ColorProvider(day = pinkDark, night = pink),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }

        Spacer(modifier = GlanceModifier.height(12))

        // Task display area
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .defaultWeight()
                .cornerRadius(16)
                .background(ColorProvider(day = white, night = darkCard))
                .padding(12),
            contentAlignment = Alignment.Center
        ) {
            when {
                taskName == null -> Text(
                    text = "Tap Roll to get a task!",
                    style = TextStyle(
                        color = ColorProvider(day = Color(0xFF8E8E9A), night = Color(0xFFB8B8C4)),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                )
                noTasks -> Text(
                    text = "No tasks available",
                    style = TextStyle(
                        color = ColorProvider(day = Color(0xFF8E8E9A), night = Color(0xFFB8B8C4)),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                )
                else -> Text(
                    text = taskName,
                    style = TextStyle(
                        color = ColorProvider(day = Color(0xFF1A1A2E), night = Color.White),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 3
                )
            }
        }

        Spacer(modifier = GlanceModifier.height(12))

        // Roll button
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .cornerRadius(14)
                .background(ColorProvider(day = pink, night = pink))
                .clickable(actionRunCallback<RollActionCallback>())
                .padding(vertical = 10),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "\uD83C\uDFB2  Roll!",
                style = TextStyle(
                    color = ColorProvider(day = white, night = white),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            )
        }
    }
}

private val Int.sp get() = androidx.compose.ui.unit.TextUnit(this.toFloat(), androidx.compose.ui.unit.TextUnitType.Sp)
