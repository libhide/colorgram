package com.madebyratik.colorgram

import android.app.Application
import org.koin.android.ext.android.startKoin

class ColorgramApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}