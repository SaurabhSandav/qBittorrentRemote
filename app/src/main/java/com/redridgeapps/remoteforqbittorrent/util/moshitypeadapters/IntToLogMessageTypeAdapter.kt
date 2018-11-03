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
            INT_NORMAL -> LogMessageType.NORMAL
            INT_INFO -> LogMessageType.INFO
            INT_WARNING -> LogMessageType.WARNING
            INT_CRITICAL -> LogMessageType.CRITICAL
            else -> throw IllegalArgumentException("Unknown argument: $value")
        }
    }

    @ToJson
    fun toJson(@EnumLogMessageType value: LogMessageType): Int {
        return when (value) {
            LogMessageType.NORMAL -> INT_NORMAL
            LogMessageType.INFO -> INT_INFO
            LogMessageType.WARNING -> INT_WARNING
            LogMessageType.CRITICAL -> INT_CRITICAL
        }
    }
}

private const val INT_NORMAL = 1
private const val INT_INFO = 2
private const val INT_WARNING = 4
private const val INT_CRITICAL = 8
