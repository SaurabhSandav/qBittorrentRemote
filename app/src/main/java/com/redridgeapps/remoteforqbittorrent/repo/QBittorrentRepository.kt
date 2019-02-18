package com.redridgeapps.remoteforqbittorrent.repo

import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService
import com.redridgeapps.remoteforqbittorrent.model.QBittorrentLog
import com.redridgeapps.remoteforqbittorrent.model.Torrent
import com.redridgeapps.remoteforqbittorrent.util.MIME_TYPE_TORRENT_FILE
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    suspend fun login(): Try<Boolean> = Try {

        val response = qBitService.login(
                baseUrl = prefRepo.baseUrl,
                username = prefRepo.username,
                password = prefRepo.password
        )

        val sid = QBittorrentService.extractSID(response.raw())

        sid?.let {
            prefRepo.initialConfigFinished = true
            prefRepo.sid = it
        }

        return@Try !sid.isNullOrBlank()
    }

    suspend fun getTorrentList(
            filter: String? = null
    ): Try<List<Torrent>> = Try {

        return@Try qBitService.getTorrentList(
                baseUrl = prefRepo.baseUrl,
                filter = filter,
                sort = prefRepo.torrentListSort,
                reverse = prefRepo.torrentListSortReverse
        )
    }

    suspend fun pause(hashes: List<String>? = null): Try<Unit> = Try {

        qBitService.pause(
                baseUrl = prefRepo.baseUrl,
                hashes = hashes?.joinToString(QBittorrentService.HASHES_SEPARATOR)
                        ?: QBittorrentService.HASHES_ALL
        )
    }

    suspend fun resume(hashes: List<String>? = null): Try<Unit> = Try {

        qBitService.resume(
                baseUrl = prefRepo.baseUrl,
                hashes = hashes?.joinToString(QBittorrentService.HASHES_SEPARATOR)
                        ?: QBittorrentService.HASHES_ALL
        )
    }

    suspend fun delete(
            deleteFiles: Boolean,
            hashes: List<String>? = null
    ): Try<Unit> = Try {

        qBitService.delete(
                baseUrl = prefRepo.baseUrl,
                hashes = hashes?.joinToString(QBittorrentService.HASHES_SEPARATOR)
                        ?: QBittorrentService.HASHES_ALL,
                deleteFiles = deleteFiles
        )
    }

    suspend fun recheck(hashes: List<String>? = null): Try<Unit> = Try {

        qBitService.recheck(
                baseUrl = prefRepo.baseUrl,
                hashes = hashes?.joinToString(QBittorrentService.HASHES_SEPARATOR)
                        ?: QBittorrentService.HASHES_ALL
        )
    }

    suspend fun addTorrentLinks(links: List<String>): Try<Unit> = Try {

        qBitService.addTorrents(
                baseUrl = prefRepo.baseUrl,
                urls = links.joinToString("\n")
        )
    }

    suspend fun addTorrentFiles(files: List<File>): Try<Unit> = Try {

        val torrents = files.map {
            val requestFile = RequestBody.create(MediaType.parse(MIME_TYPE_TORRENT_FILE), it)
            MultipartBody.Part.createFormData(LABEL_PARAMETER_NAME_TORRENTS, it.name, requestFile)
        }

        qBitService.addTorrents(
                baseUrl = prefRepo.baseUrl,
                torrents = torrents
        )
    }

    suspend fun getLog(
            normal: Boolean = true,
            info: Boolean = true,
            warning: Boolean = true,
            critical: Boolean = true,
            lastId: Int = -1
    ): Try<List<QBittorrentLog>> = Try {

        return@Try qBitService.getLog(
                baseUrl = prefRepo.baseUrl,
                normal = normal,
                info = info,
                warning = warning,
                critical = critical,
                lastKnownId = lastId
        )
    }
}

private const val LABEL_PARAMETER_NAME_TORRENTS = "torrents"
