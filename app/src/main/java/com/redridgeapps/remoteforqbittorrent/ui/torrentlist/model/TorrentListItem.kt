package com.redridgeapps.remoteforqbittorrent.ui.torrentlist.model

import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
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
) {

    object DiffCallback : DiffUtil.ItemCallback<TorrentListItem>() {
        override fun areItemsTheSame(oldItem: TorrentListItem, newItem: TorrentListItem): Boolean {
            return oldItem.hash == newItem.hash
        }

        override fun areContentsTheSame(oldItem: TorrentListItem, newItem: TorrentListItem): Boolean {
            return oldItem.state == newItem.state && !oldItem.state.isVolatile()
        }
    }
}
