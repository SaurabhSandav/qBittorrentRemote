package com.redridgeapps.remoteforqbittorrent.di

import android.app.Application
import com.redridgeapps.remoteforqbittorrent.App
import com.redridgeapps.remoteforqbittorrent.di.module.ActivityBuilder
import com.redridgeapps.remoteforqbittorrent.di.module.AppModule
import com.redridgeapps.remoteforqbittorrent.di.module.FragmentBuilder
import com.redridgeapps.remoteforqbittorrent.di.module.ViewModelBuilder
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
            ViewModelBuilder::class,
            FragmentBuilder::class,
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
