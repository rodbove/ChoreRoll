package com.choreroll.app

import android.app.Application
import com.choreroll.app.di.AppContainer

class ChoreRollApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
