package com.redridgeapps.remoteforqbittorrent.di.module

import com.redridgeapps.remoteforqbittorrent.MainActivity
import com.redridgeapps.remoteforqbittorrent.di.PerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector()
    abstract fun bindMainActivity(): MainActivity
}
