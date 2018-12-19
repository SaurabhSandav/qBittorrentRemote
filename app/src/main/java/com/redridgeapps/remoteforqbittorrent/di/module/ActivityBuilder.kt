package com.redridgeapps.remoteforqbittorrent.di.module

import com.redridgeapps.remoteforqbittorrent.di.PerActivity
import com.redridgeapps.remoteforqbittorrent.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun bindMainActivity(): MainActivity
}
