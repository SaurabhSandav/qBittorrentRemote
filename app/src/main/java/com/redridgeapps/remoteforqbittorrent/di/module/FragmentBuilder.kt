package com.redridgeapps.remoteforqbittorrent.di.module

import com.redridgeapps.remoteforqbittorrent.di.PerFragment
import com.redridgeapps.remoteforqbittorrent.ui.config.ConfigFragment
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.TorrentListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @PerFragment
    @ContributesAndroidInjector()
    abstract fun bindTorrentListFragment(): TorrentListFragment

    @PerFragment
    @ContributesAndroidInjector()
    abstract fun bindConfigFragment(): ConfigFragment
}
