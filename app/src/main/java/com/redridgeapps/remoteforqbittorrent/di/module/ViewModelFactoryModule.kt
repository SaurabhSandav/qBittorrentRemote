package com.redridgeapps.remoteforqbittorrent.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redridgeapps.remoteforqbittorrent.di.ViewModelKey
import com.redridgeapps.remoteforqbittorrent.ui.base.ViewModelFactory
import com.redridgeapps.remoteforqbittorrent.ui.config.ConfigViewModel
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.TorrentListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TorrentListViewModel::class)
    abstract fun bindTorrentListViewModel(viewModel: TorrentListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConfigViewModel::class)
    abstract fun bindConfigViewModel(viewModel: ConfigViewModel): ViewModel
}
