package com.asusuigbo.frank.asusuigbo

import android.app.Application
import timber.log.Timber

class AsusuIgboApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}