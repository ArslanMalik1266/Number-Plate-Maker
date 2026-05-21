package com.webscare.numberplatemaker

import android.app.Application
import com.webscare.numberplatemaker.data.local.DatabaseBuilder.appContext
import com.webscare.numberplatemaker.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        appContext = this
        startKoin {
            androidContext(this@MyApp)
            modules(appModules)
        }
    }
}