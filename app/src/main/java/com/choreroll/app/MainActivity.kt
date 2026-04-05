package com.choreroll.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.choreroll.app.ui.navigation.ChoreRollNavGraph
import com.choreroll.app.ui.theme.ChoreRollTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val container = (application as ChoreRollApp).container
        setContent {
            ChoreRollTheme {
                ChoreRollNavGraph(container = container)
            }
        }
    }
}
