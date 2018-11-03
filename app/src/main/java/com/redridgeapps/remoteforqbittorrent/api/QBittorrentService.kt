package com.redridgeapps.remoteforqbittorrent.api

import arrow.core.Option
import arrow.core.toOption
import com.redridgeapps.remoteforqbittorrent.model.QBittorrentLog
import com.redridgeapps.remoteforqbittorrent.model.Torrent
import kotlinx.coroutines.Deferred
import okhttp3.HttpUrl
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Url

interface QBittorrentService {

    companion object {

        const val DEFAULT_BASE_URL = "http://localhost:8080/"
        const val DEFAULT_USERNAME = "admin"
        const val DEFAULT_PASSWORD = "adminadmin"

        const val HASHES_ALL = "all"
        const val HASHES_SEPARATOR = "|"

        const val AUTH_LOGIN = "api/v2/auth/login"
        private const val LOG_MAIN = "api/v2/log/main"
        private const val TORRENTS_INFO = "api/v2/torrents/info"
        private const val TORRENTS_ADD = "api/v2/torrents/add"
        private const val TORRENTS_PAUSE = "api/v2/torrents/pause"
        private const val TORRENTS_RESUME = "api/v2/torrents/resume"
        private const val TORRENTS_DELETE = "api/v2/torrents/delete"
        private const val TORRENTS_RECHECK = "api/v2/torrents/recheck"

        private const val HEADER_LABEL_REFERER = "Referer"
        private const val HEADER_SET_COOKIE = "Set-Cookie"

        private const val SCHEME_HTTP = "http"
        private const val SCHEME_HTTPS = "https"

        private const val SID_REGEX_PATTERN = """SID=(.+?);"""

        fun buildBaseURL(host: String, port: Int, useHttps: Boolean): String {
            return HttpUrl.Builder()
                    .scheme(getScheme(useHttps))
                    .host(host)
                    .port(port)
                    .build()
                    .url()
                    .toString()
        }

        fun buildURL(baseUrl: String, path: String): String = baseUrl + path

        fun extractSID(resp: okhttp3.Response): Option<String> {
            return resp.headers().get(HEADER_SET_COOKIE)
                    .toOption()
                    .map { Regex(SID_REGEX_PATTERN).find(it)?.groupValues?.get(1) }
                    .map { "SID=$it" }
        }

        private fun getScheme(useHttps: Boolean) = if (useHttps) SCHEME_HTTPS else SCHEME_HTTP
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
    fun getLog(
            @Header(HEADER_LABEL_REFERER) baseUrl: String,
            @Url url: String = buildURL(baseUrl, LOG_MAIN),
            @Query("normal") normal: Boolean? = null,
            @Query("info") info: Boolean? = null,
            @Query("warning") warning: Boolean? = null,
            @Query("critical") critical: Boolean? = null,
            @Query("last_known_id") lastKnownId: Int? = null
    ): Deferred<List<QBittorrentLog>>

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

    @FormUrlEncoded
    @POST
    fun pause(
            @Header(HEADER_LABEL_REFERER) baseUrl: String,
            @Url url: String = buildURL(baseUrl, TORRENTS_PAUSE),
            @Field("hashes") hashes: String
    ): Deferred<Void>

    @FormUrlEncoded
    @POST
    fun resume(
            @Header(HEADER_LABEL_REFERER) baseUrl: String,
            @Url url: String = buildURL(baseUrl, TORRENTS_RESUME),
            @Field("hashes") hashes: String
    ): Deferred<Void>

    @FormUrlEncoded
    @POST
    fun delete(
            @Header(HEADER_LABEL_REFERER) baseUrl: String,
            @Url url: String = buildURL(baseUrl, TORRENTS_DELETE),
            @Field("hashes") hashes: String,
            @Field("deleteFiles") deleteFiles: Boolean
    ): Deferred<Void>

    @FormUrlEncoded
    @POST
    fun recheck(
            @Header(HEADER_LABEL_REFERER) baseUrl: String,
            @Url url: String = buildURL(baseUrl, TORRENTS_RECHECK),
            @Field("hashes") hashes: String
    ): Deferred<Void>

    @Multipart
    @POST
    fun addTorrents(
            @Header(HEADER_LABEL_REFERER) baseUrl: String,
            @Url url: String = buildURL(baseUrl, TORRENTS_ADD),
            @Part("urls") urls: String? = null,
            @Part torrents: List<MultipartBody.Part>? = null,
            @Part("savepath") savepath: String? = null,
            @Part("cookie") cookie: String? = null,
            @Part("category") category: String? = null,
            @Part("skip_checking") skip_checking: Boolean? = null,
            @Part("paused") paused: Boolean? = null,
            @Part("root_folder") rootFolder: Boolean? = null,
            @Part("rename") rename: String? = null,
            @Part("upLimit") upLimit: Int? = null,
            @Part("dlLimit") dlLimit: Int? = null,
            @Part("sequentialDownload") sequentialDownload: Boolean? = null,
            @Part("firstLastPiecePrio") firstLastPiecePriority: Boolean? = null
    ): Deferred<Void>
}
