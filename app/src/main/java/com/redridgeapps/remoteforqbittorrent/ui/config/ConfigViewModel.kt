package com.redridgeapps.remoteforqbittorrent.ui.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.repo.PreferenceRepository
import com.redridgeapps.remoteforqbittorrent.repo.QBittorrentRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConfigViewModel @Inject constructor(
        private val qBitRepo: QBittorrentRepository,
        private val prefRepo: PreferenceRepository
) : BaseViewModel() {

    private val _loginResultLiveData = MutableLiveData<Try<Boolean>>()

    val loginResultLiveData: LiveData<Try<Boolean>> = _loginResultLiveData

    fun login(
            host: String,
            port: Int,
            useHttps: Boolean,
            username: String,
            password: String
    ) = viewModelScope.launch {
        qBitRepo.saveConfig(host, port, useHttps, username, password)

        val result = qBitRepo.login()

        _loginResultLiveData.value = result
    }

    fun isInitialConfigFinished() = prefRepo.initialConfigFinished
}
