package com.redridgeapps.remoteforqbittorrent.ui.log.model

import androidx.recyclerview.widget.DiffUtil

data class LogListItem(
        val id: Int,
        val message: String,
        val timestamp: String,
        val type: Int
) {

    object DiffCallback : DiffUtil.ItemCallback<LogListItem>() {
        override fun areItemsTheSame(oldItem: LogListItem, newItem: LogListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LogListItem, newItem: LogListItem) = true
    }
}
