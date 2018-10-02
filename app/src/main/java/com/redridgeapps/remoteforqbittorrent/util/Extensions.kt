package com.redridgeapps.remoteforqbittorrent.util

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment

val BaseFragment.compatActivity
    get() = requireActivity() as AppCompatActivity

fun <T : ViewModel> BaseFragment.getViewModel(viewModelClass: Class<T>): T {
    return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
}
