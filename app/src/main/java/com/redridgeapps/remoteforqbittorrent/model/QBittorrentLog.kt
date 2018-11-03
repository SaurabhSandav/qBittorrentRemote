package com.redridgeapps.remoteforqbittorrent.model

import com.redridgeapps.remoteforqbittorrent.util.moshitypeadapters.EnumLogMessageType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QBittorrentLog(
        @Json(name = "id") val id: Int,
        @Json(name = "message") val message: String,
        @Json(name = "timestamp") val timestamp: Long,
        @EnumLogMessageType @Json(name = "type") val type: LogMessageType
)

enum class LogMessageType {
    NORMAL,
    INFO,
    WARNING,
    CRITICAL;
}
