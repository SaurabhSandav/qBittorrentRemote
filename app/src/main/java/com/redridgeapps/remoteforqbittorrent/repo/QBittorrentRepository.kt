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

    suspend fun getTorrentList(
            filter: String? = null
    ): Try<List<Torrent>> {

        val request = qBitService.getTorrentList(
                baseUrl = prefRepo.baseUrl,
                filter = filter,
                sort = prefRepo.torrentListSort,
                reverse = prefRepo.torrentListSortReverse
        )

        return request.processResult()
    }

    suspend fun pause(hashes: List<String>? = null): Try<Unit> {
        val request = qBitService.pause(
                baseUrl = prefRepo.baseUrl,
                hashes = hashes?.joinToString(QBittorrentService.HASHES_SEPARATOR)
                        ?: QBittorrentService.HASHES_ALL
        )

        return request.processResult().map { Unit }
    }

    suspend fun resume(hashes: List<String>? = null): Try<Unit> {
        val request = qBitService.resume(
                baseUrl = prefRepo.baseUrl,
                hashes = hashes?.joinToString(QBittorrentService.HASHES_SEPARATOR)
                        ?: QBittorrentService.HASHES_ALL
        )

        return request.processResult().map { Unit }
    }

    private suspend fun <T> Deferred<Response<T>>.processResponse() = processResult()

    private suspend fun <T> Deferred<T>.processResult() = Try { await() }
}
