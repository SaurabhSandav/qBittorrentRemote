package com.redridgeapps.remoteforqbittorrent

import android.app.Activity
import android.app.Application
import com.redridgeapps.remoteforqbittorrent.di.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this@App)) return
        LeakCanary.install(this)

        setupDaggerInjection()
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

    private fun setupDaggerInjection() {
        return DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this)
    }
}
