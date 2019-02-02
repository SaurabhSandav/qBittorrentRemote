package com.redridgeapps.remoteforqbittorrent.di.module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.redridgeapps.remoteforqbittorrent.ui.base.CustomNavHostFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.dagger.DaggerFragmentFactory
import com.redridgeapps.remoteforqbittorrent.ui.config.ConfigFragment
import com.redridgeapps.remoteforqbittorrent.ui.log.LogFragment
import com.redridgeapps.remoteforqbittorrent.ui.settings.SettingsFragment
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.TorrentListFragment
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
internal annotation class FragmentKey(val value: KClass<out Fragment>)

@Module
abstract class FragmentBuilder {

    @Binds
    abstract fun bindFragmentFactory(factory: DaggerFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(CustomNavHostFragment::class)
    abstract fun bindCustomNavHostFragment(factory: CustomNavHostFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(TorrentListFragment::class)
    abstract fun bindTorrentListFragment(fragment: TorrentListFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(ConfigFragment::class)
    abstract fun bindConfigFragment(fragment: ConfigFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(LogFragment::class)
    abstract fun bindLogFragment(fragment: LogFragment): Fragment

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun bindSettingsFragment(fragment: SettingsFragment): Fragment
}
