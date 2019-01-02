package com.redridgeapps.remoteforqbittorrent.di.module

import androidx.fragment.app.FragmentFactory
import com.redridgeapps.remoteforqbittorrent.di.PerFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragmentMarker
import com.redridgeapps.remoteforqbittorrent.ui.base.CustomNavHostFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.dagger.DaggerFragmentFactory
import com.redridgeapps.remoteforqbittorrent.ui.config.ConfigFragment
import com.redridgeapps.remoteforqbittorrent.ui.log.LogFragment
import com.redridgeapps.remoteforqbittorrent.ui.settings.SettingsFragment
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.TorrentListFragment
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.android.ContributesAndroidInjector
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
internal annotation class FragmentKey(val value: KClass<out BaseFragmentMarker>)

@Module
abstract class FragmentBuilder {

    // Constructor Injection

    @Binds
    abstract fun bindFragmentFactory(factory: DaggerFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(TorrentListFragment::class)
    abstract fun bindTorrentListFragment(fragment: TorrentListFragment): BaseFragmentMarker

    @Binds
    @IntoMap
    @FragmentKey(ConfigFragment::class)
    abstract fun bindConfigFragment(fragment: ConfigFragment): BaseFragmentMarker

    @Binds
    @IntoMap
    @FragmentKey(LogFragment::class)
    abstract fun bindLogFragment(fragment: LogFragment): BaseFragmentMarker

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun bindSettingsFragment(fragment: SettingsFragment): BaseFragmentMarker

    // Field Injection

    @PerFragment
    @ContributesAndroidInjector()
    abstract fun bindCustomNavHostFragment(): CustomNavHostFragment
}
