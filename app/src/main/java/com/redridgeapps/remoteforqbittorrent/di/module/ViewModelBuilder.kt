package com.redridgeapps.remoteforqbittorrent.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redridgeapps.remoteforqbittorrent.ui.base.DaggerViewModelFactory
import com.redridgeapps.remoteforqbittorrent.ui.config.ConfigViewModel
import com.redridgeapps.remoteforqbittorrent.ui.log.LogViewModel
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.TorrentListViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@MustBeDocumented
@Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelBuilder {

    @Binds
    abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TorrentListViewModel::class)
    abstract fun bindTorrentListViewModel(viewModel: TorrentListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConfigViewModel::class)
    abstract fun bindConfigViewModel(viewModel: ConfigViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LogViewModel::class)
    abstract fun bindLogViewModel(viewModel: LogViewModel): ViewModel
}
