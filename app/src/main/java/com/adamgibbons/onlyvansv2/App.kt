package com.adamgibbons.onlyvansv2

import android.app.Application
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        Timber.i("OnlyVansV2 app started!")
    }
}