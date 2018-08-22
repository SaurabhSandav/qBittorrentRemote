package com.redridgeapps.remoteforqbittorrent.util.moshitypeadapters

import com.redridgeapps.remoteforqbittorrent.model.LogMessageType
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

@JsonQualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class EnumLogMessageType

class IntToLogMessageTypeAdapter {

    @FromJson
    @EnumLogMessageType
    fun fromJson(value: Int): LogMessageType {
        return when (value) {
            1 -> LogMessageType.NORMAL
            2 -> LogMessageType.INFO
            4 -> LogMessageType.WARNING
            8 -> LogMessageType.CRITICAL
            else -> throw IllegalArgumentException("Unknown argument: $value")
        }
    }

    @ToJson
    fun toJson(@EnumLogMessageType value: LogMessageType): Int {
        return when (value) {
            LogMessageType.NORMAL -> 1
            LogMessageType.INFO -> 2
            LogMessageType.WARNING -> 4
            LogMessageType.CRITICAL -> 8
        }
    }
}