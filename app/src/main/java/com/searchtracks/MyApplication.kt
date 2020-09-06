package com.searchtracks

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.searchtracks.dagger.AppComponent
import com.searchtracks.dagger.AppModule
import com.searchtracks.dagger.DaggerAppComponent
import com.searchtracks.dagger.DatabaseModule

class MyApplication : Application(){

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        initComponent()
    }

    private fun initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .databaseModule(DatabaseModule(this))
                .build()
        appComponent.inject(this)
        Fresco.initialize(this)
        MultiDex.install(this)
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


}