package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.ListItemTorrentBinding
import com.redridgeapps.remoteforqbittorrent.di.PerFragment
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.model.TorrentListItem
import com.redridgeapps.remoteforqbittorrent.util.recyclerDataBindingInflate
import javax.inject.Inject

@PerFragment
class TorrentListAdapter @Inject constructor() :
        ListAdapter<TorrentListItem, TorrentListAdapter.ViewHolder>(TorrentListItem.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ListItemTorrentBinding = parent.recyclerDataBindingInflate(R.layout.list_item_torrent)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ListItemTorrentBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(torrent: TorrentListItem) {
            binding.torrent = torrent
            binding.executePendingBindings()
        }
    }
}