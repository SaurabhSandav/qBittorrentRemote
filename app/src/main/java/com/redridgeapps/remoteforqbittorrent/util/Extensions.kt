package com.redridgeapps.remoteforqbittorrent.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment

fun <T : ViewModel> BaseFragment.getViewModel(viewModelClass: Class<T>): T {
    return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
}
