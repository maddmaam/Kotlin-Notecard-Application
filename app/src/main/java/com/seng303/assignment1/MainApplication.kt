package com.seng303.assignment1

import android.app.Application
import com.seng303.assignment1.datastore.dataAccessModule
import com.seng303.assignment1.datastore.playerScoreAccessModule
import kotlinx.coroutines.FlowPreview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    @OptIn(FlowPreview::class)
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApplication)
            modules(dataAccessModule)
//            modules(playerScoreAccessModule)
        }
    }
}