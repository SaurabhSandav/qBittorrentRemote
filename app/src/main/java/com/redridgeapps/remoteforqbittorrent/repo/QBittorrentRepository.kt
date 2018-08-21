package com.redridgeapps.remoteforqbittorrent.repo

import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QBittorrentRepository @Inject constructor(
        private val prefRepo: PreferenceRepository
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
}
