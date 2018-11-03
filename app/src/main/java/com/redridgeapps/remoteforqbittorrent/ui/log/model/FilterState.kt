package com.redridgeapps.remoteforqbittorrent.ui.log.model

data class FilterState(
        val normal: Boolean = true,
        val info: Boolean = true,
        val warning: Boolean = true,
        val critical: Boolean = true
)
