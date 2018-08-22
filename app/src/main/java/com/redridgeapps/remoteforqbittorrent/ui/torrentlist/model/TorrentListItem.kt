package com.redridgeapps.remoteforqbittorrent.ui.torrentlist.model

import androidx.annotation.StringRes
import com.redridgeapps.remoteforqbittorrent.model.TorrentState

data class TorrentListItem(
        val hash: String,
        val title: String,
        val progress: Int,
        val progressStr: String,
        val state: TorrentState,
        @StringRes val statusResId: Int,
        val dlSpeed: String,
        val upSpeed: String
)
