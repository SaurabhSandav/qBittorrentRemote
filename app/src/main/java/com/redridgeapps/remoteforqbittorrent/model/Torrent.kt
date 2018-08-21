package com.redridgeapps.remoteforqbittorrent.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Torrent(
        @Json(name = "hash") val hash: String,
        @Json(name = "name") val name: String,
        @Json(name = "size") val size: Long,
        @Json(name = "progress") val progress: Float,
        @Json(name = "dlspeed") val dlSpeed: Long,
        @Json(name = "upspeed") val upSpeed: Long,
        @Json(name = "priority") val priority: Int,
        @Json(name = "num_seeds") val numSeeds: Int,
        @Json(name = "num_complete") val numComplete: Int,
        @Json(name = "num_leechs") val numLeechs: Int,
        @Json(name = "num_incomplete") val numIncomplete: Int,
        @Json(name = "ratio") val ratio: Float,
        @Json(name = "eta") val eta: Long,
        @Json(name = "state") val state: TorrentState,
        @Json(name = "seq_dl") val seqDl: Boolean,
        @Json(name = "f_l_piece_prio") val flPiecePriority: Boolean? = null,
        @Json(name = "category") val category: String,
        @Json(name = "super_seeding") val superSeeding: Boolean,
        @Json(name = "force_start") val forceStart: Boolean
)

enum class TorrentState {
    @Json(name = "error")
    ERROR,
    @Json(name = "pausedUP")
    PAUSED_UP,
    @Json(name = "pausedDL")
    PAUSED_DL,
    @Json(name = "queuedUP")
    QUEUED_UP,
    @Json(name = "queuedDL")
    QUEUED_DL,
    @Json(name = "uploading")
    UPLOADING,
    @Json(name = "stalledUP")
    STALLED_UP,
    @Json(name = "checkingUP")
    CHECKING_UP,
    @Json(name = "checkingDL")
    CHECKING_DL,
    @Json(name = "downloading")
    DOWNLOADING,
    @Json(name = "stalledDL")
    STALLED_DL,
    @Json(name = "metaDL")
    META_DL;
}