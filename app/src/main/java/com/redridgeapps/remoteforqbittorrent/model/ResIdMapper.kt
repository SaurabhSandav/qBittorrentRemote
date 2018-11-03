package com.redridgeapps.remoteforqbittorrent.model

import com.redridgeapps.remoteforqbittorrent.R

object ResIdMapper {

    fun TorrentState.toResId() = when (this) {
        TorrentState.ERROR -> R.string.state_error
        TorrentState.PAUSED_UP -> R.string.state_paused_up
        TorrentState.PAUSED_DL -> R.string.state_paused
        TorrentState.QUEUED_UP -> R.string.state_queued_up
        TorrentState.QUEUED_DL -> R.string.state_queued_dl
        TorrentState.UPLOADING -> R.string.state_seeding
        TorrentState.STALLED_UP -> R.string.state_stalled_up
        TorrentState.CHECKING_UP -> R.string.state_checking
        TorrentState.CHECKING_DL -> R.string.state_checking
        TorrentState.DOWNLOADING -> R.string.state_downloading
        TorrentState.STALLED_DL -> R.string.state_stalled_dl
        TorrentState.META_DL -> R.string.state_fetching_metadata
    }

    fun LogMessageType.toResId(): Int {
        return when (this) {
            LogMessageType.NORMAL -> R.string.log_type_normal
            LogMessageType.INFO -> R.string.log_type_info
            LogMessageType.WARNING -> R.string.log_type_warning
            LogMessageType.CRITICAL -> R.string.log_type_critical
        }
    }
}
