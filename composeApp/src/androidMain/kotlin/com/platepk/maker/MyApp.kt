package com.platepk.maker

import android.app.Application
import com.platepk.maker.data.local.DatabaseBuilder.appContext
import com.platepk.maker.di.appModules
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