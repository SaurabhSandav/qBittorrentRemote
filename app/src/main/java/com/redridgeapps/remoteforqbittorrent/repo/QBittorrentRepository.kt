package com.redridgeapps.remoteforqbittorrent.repo

import arrow.core.Some
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService
import com.redridgeapps.remoteforqbittorrent.model.Torrent
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

    suspend fun login(): Try<Unit> {

        val request = qBitService.login(
                baseUrl = prefRepo.baseUrl,
                username = prefRepo.username,
                password = prefRepo.password
        )

        return request.processResponse().map { response ->
            val sid = QBittorrentService.extractSID(response.raw())
            if (sid is Some) {
                prefRepo.initialConfigFinished = true
                prefRepo.sid = sid.t
            }
        }
    }

    suspend fun getTorrentList(): Try<List<Torrent>> {

        val request = qBitService.getTorrentList(
                baseUrl = prefRepo.baseUrl
        )

        return request.processResult()
    }

    private suspend fun <T> Deferred<Response<T>>.processResponse() = processResult()

    private suspend fun <T> Deferred<T>.processResult() = Try { await() }
}
