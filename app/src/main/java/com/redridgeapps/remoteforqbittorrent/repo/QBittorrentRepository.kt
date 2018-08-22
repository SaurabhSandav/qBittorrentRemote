package com.redridgeapps.remoteforqbittorrent.repo

import arrow.core.Some
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService
import com.redridgeapps.remoteforqbittorrent.model.QBittorrentLog
import com.redridgeapps.remoteforqbittorrent.model.Torrent
import com.redridgeapps.remoteforqbittorrent.util.MIME_TYPE_TORRENT_FILE
import kotlinx.coroutines.experimental.Deferred
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File
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

    suspend fun addTorrentLinks(links: List<String>): Try<Unit> {
        val request = qBitService.addTorrents(
                baseUrl = prefRepo.baseUrl,
                urls = links.joinToString("\n")
        )

        return request.processResult().map { Unit }
    }

    suspend fun addTorrentFiles(files: List<File>): Try<Unit> {
        val torrents = files.map {
            val requestFile = RequestBody.create(MediaType.parse(MIME_TYPE_TORRENT_FILE), it)
            MultipartBody.Part.createFormData(LABEL_PARAMETER_NAME_TORRENTS, it.name, requestFile)
        }

        val request = qBitService.addTorrents(
                baseUrl = prefRepo.baseUrl,
                torrents = torrents
        )

        return request.processResult().map { Unit }
    }

    suspend fun getLog(lastId: Int = -1): Try<List<QBittorrentLog>> {
        val request = qBitService.getLog(
                baseUrl = prefRepo.baseUrl,
                lastKnownId = lastId
        )

        return request.processResult()
    }

    private suspend fun <T> Deferred<Response<T>>.processResponse() = processResult()

    private suspend fun <T> Deferred<T>.processResult() = Try { await() }
}

private const val LABEL_PARAMETER_NAME_TORRENTS = "torrents"
