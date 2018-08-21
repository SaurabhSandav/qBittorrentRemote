package com.redridgeapps.remoteforqbittorrent.api

import com.redridgeapps.remoteforqbittorrent.model.Torrent
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.*

interface QBittorrentService {

    companion object {

        const val DEFAULT_BASE_URL = "http://localhost:8080/"
        const val DEFAULT_USERNAME = "admin"
        const val DEFAULT_PASSWORD = "adminadmin"

        const val HASHES_ALL = "all"
        const val HASHES_SEPARATOR = "|"

        const val AUTH_LOGIN = "api/v2/auth/login"
        private const val TORRENTS_INFO = "api/v2/torrents/info"

        private const val HEADER_LABEL_REFERER = "Referer"

        private fun buildURL(baseUrl: String, path: String): String = baseUrl + path
    }

    object Filter {
        const val ALL = "all"
        const val DOWNLOADING = "downloading"
        const val COMPLETED = "completed"
        const val PAUSED = "paused"
        const val ACTIVE = "active"
        const val INACTIVE = "inactive"
    }

    object Sort {
        const val HASH = "hash"
        const val NAME = "name"
        const val SIZE = "size"
        const val PROGRESS = "progress"
        const val DLSPEED = "dlspeed"
        const val UPSPEED = "upspeed"
        const val PRIORITY = "priority"
        const val NUM_SEEDS = "num_seeds"
        const val NUM_COMPLETE = "num_complete"
        const val NUM_LEECHS = "num_leechs"
        const val NUM_INCOMPLETE = "num_incomplete"
        const val RATIO = "ratio"
        const val ETA = "eta"
        const val STATE = "state"
        const val SEQ_DL = "seq_dl"
        const val F_L_PIECE_PRIO = "f_l_piece_prio"
        const val CATEGORY = "category"
        const val SUPER_SEEDING = "super_seeding"
        const val FORCE_START = "force_start"
    }

    @FormUrlEncoded
    @POST
    fun login(
            @Header(HEADER_LABEL_REFERER) baseUrl: String,
            @Url url: String = buildURL(baseUrl, AUTH_LOGIN),
            @Field("username") username: String,
            @Field("password") password: String
    ): Deferred<Response<Void>>

    @GET
    fun getTorrentList(
            @Header(HEADER_LABEL_REFERER) baseUrl: String,
            @Url url: String = buildURL(baseUrl, TORRENTS_INFO),
            @Query("filter") filter: String? = null,
            @Query("category") category: String? = null,
            @Query("sort") sort: String? = null,
            @Query("reverse") reverse: Boolean? = null,
            @Query("limit") limit: Int? = null,
            @Query("offset") offset: Int? = null,
            @Query("hashes") hashes: String? = null
    ): Deferred<List<Torrent>>
}
