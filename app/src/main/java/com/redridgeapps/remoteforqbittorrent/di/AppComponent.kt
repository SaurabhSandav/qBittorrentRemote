package com.redridgeapps.remoteforqbittorrent.di

import android.app.Application
import com.redridgeapps.remoteforqbittorrent.App
import com.redridgeapps.remoteforqbittorrent.di.module.ActivityBuilder
import com.redridgeapps.remoteforqbittorrent.di.module.AppModule
import com.redridgeapps.remoteforqbittorrent.di.module.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            ActivityBuilder::class,
            ViewModelFactoryModule::class,
            AppModule::class
        ]
)
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): AppComponent.Builder

        fun build(): AppComponent
    }
}