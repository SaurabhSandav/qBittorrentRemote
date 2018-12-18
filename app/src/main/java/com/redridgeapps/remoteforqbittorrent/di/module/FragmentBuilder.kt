package com.redridgeapps.remoteforqbittorrent.di.module

import androidx.fragment.app.FragmentFactory
import com.redridgeapps.remoteforqbittorrent.di.FragmentKey
import com.redridgeapps.remoteforqbittorrent.di.PerFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.CustomNavHostFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.DaggerFragmentFactory
import com.redridgeapps.remoteforqbittorrent.ui.config.ConfigFragment
import com.redridgeapps.remoteforqbittorrent.ui.log.LogFragment
import com.redridgeapps.remoteforqbittorrent.ui.settings.SettingsFragment
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.TorrentListFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class FragmentBuilder {

    // Constructor Injection

    @Binds
    abstract fun bindFragmentFactory(factory: DaggerFragmentFactory): FragmentFactory

    @Binds
    @IntoMap
    @FragmentKey(TorrentListFragment::class)
    abstract fun bindTorrentListFragment(fragment: TorrentListFragment): BaseFragment

    @Binds
    @IntoMap
    @FragmentKey(ConfigFragment::class)
    abstract fun bindConfigFragment(fragment: ConfigFragment): BaseFragment

    @Binds
    @IntoMap
    @FragmentKey(LogFragment::class)
    abstract fun bindLogFragment(fragment: LogFragment): BaseFragment

    // Field Injection

    @PerFragment
    @ContributesAndroidInjector()
    abstract fun bindCustomNavHostFragment(): CustomNavHostFragment

    @PerFragment
    @ContributesAndroidInjector()
    abstract fun bindSettingsFragment(): SettingsFragment
}
