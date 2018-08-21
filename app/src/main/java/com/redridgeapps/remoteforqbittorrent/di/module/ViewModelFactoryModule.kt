package com.redridgeapps.remoteforqbittorrent.di.module

import androidx.lifecycle.ViewModelProvider
import com.redridgeapps.remoteforqbittorrent.ui.base.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
