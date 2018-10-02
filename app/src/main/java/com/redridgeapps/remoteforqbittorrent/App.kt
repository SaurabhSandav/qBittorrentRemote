package com.redridgeapps.remoteforqbittorrent

import android.app.Application
import com.squareup.leakcanary.LeakCanary

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this@App)) return
        LeakCanary.install(this)
    }
}
