package com.redridgeapps.remoteforqbittorrent.repo

import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QBittorrentRepository @Inject constructor(
        private val prefRepo: PreferenceRepository,
        private val qBitService: QBittorrentService
) {

    fun saveConfig(
            host: String,
            port: Int,
            useHttps: Boolean,
            usernameStr: String,
            passwordStr: String
    ) {
        prefRepo.run {
            baseUrl = QBittorrentService.buildBaseURL(host, port, useHttps)
            username = usernameStr
            password = passwordStr
        }
    }

    private suspend fun <T> Deferred<Response<T>>.processResponse() = processResult()

    private suspend fun <T> Deferred<T>.processResult() = Try { await() }
}
