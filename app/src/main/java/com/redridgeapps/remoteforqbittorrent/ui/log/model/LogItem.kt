package com.redridgeapps.remoteforqbittorrent.ui.log.model

data class LogListItem(
        val id: Int,
        val message: String,
        val timestamp: String,
        val type: Int
)