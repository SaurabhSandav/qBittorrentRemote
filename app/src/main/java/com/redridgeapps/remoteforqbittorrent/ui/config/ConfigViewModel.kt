package com.redridgeapps.remoteforqbittorrent.ui.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.repo.QBittorrentRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseViewModel
import com.redridgeapps.remoteforqbittorrent.util.asMutable
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfigViewModel @Inject constructor(
        private val qBitRepo: QBittorrentRepository
) : BaseViewModel() {

    val loginResultLiveData: LiveData<Try<Unit>> = MutableLiveData()

    fun login(
            host: String,
            port: Int,
            useHttps: Boolean,
            username: String,
            password: String
    ) = viewModelScope.launch {
        qBitRepo.saveConfig(host, port, useHttps, username, password)

        val result = qBitRepo.login()

        loginResultLiveData.asMutable().setValue(result)
    }
}
