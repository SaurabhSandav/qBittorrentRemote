package com.redridgeapps.remoteforqbittorrent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _filterLiveData = MutableLiveData<String>()
    val filterLiveData: LiveData<String> = _filterLiveData

    fun setFilter(filter: String) {
        _filterLiveData.value = filter
    }
}
