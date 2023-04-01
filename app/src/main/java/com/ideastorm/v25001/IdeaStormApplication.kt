package com.ideastorm.v25001

import android.app.Application
import org.koin.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class IdeaStormApplication : Application() {

    override fun onCreate() {
        super.onCreate()


        // Koin Implementation
        GlobalContext.startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@IdeaStormApplication)
            modules(appModule)
        }
    }
}
