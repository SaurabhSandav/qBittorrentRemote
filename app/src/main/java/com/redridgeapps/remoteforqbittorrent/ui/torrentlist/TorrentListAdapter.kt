package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import android.graphics.Color
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.ListItemTorrentBinding
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.model.TorrentListItem
import com.redridgeapps.remoteforqbittorrent.util.recyclerDataBindingInflate
import javax.inject.Inject

class TorrentListAdapter @Inject constructor()
    : ListAdapter<TorrentListItem, TorrentListAdapter.TorrentViewHolder>(TorrentListItem.DiffCallback) {

    var torrentList: List<TorrentListItem> = ArrayList()
    var selectionTracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentViewHolder {
        val binding: ListItemTorrentBinding = parent.recyclerDataBindingInflate(R.layout.list_item_torrent)
        return TorrentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TorrentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, selectionTracker?.isSelected(item.hash) ?: false)
    }

    override fun submitList(list: List<TorrentListItem>?) {
        super.submitList(list)
        torrentList = list!!
    }

    fun setupSelectionTracker(
            recyclerView: RecyclerView,
            selectionObserver: SelectionTracker.SelectionObserver<String>
    ): SelectionTracker<String> {
        selectionTracker = SelectionTracker.Builder<String>(
                javaClass.simpleName,
                recyclerView,
                TorrentKeyProvider(this),
                TorrentDetailsLookup(recyclerView),
                StorageStrategy.createStringStorage()
        ).build()

        selectionTracker!!.addObserver(selectionObserver)

        return selectionTracker!!
    }

    inner class TorrentViewHolder(private val binding: ListItemTorrentBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(torrent: TorrentListItem, isActive: Boolean) {
            binding.torrent = torrent
            binding.executePendingBindings()

            binding.root.setBackgroundColor(if (isActive) Color.LTGRAY else Color.TRANSPARENT)
        }

        fun getItemDetails(): TorrentListItemDetails {
            return TorrentListItemDetails(adapterPosition, torrentList[adapterPosition].hash)
        }
    }
}

class TorrentListItemDetails(
        private val itemPosition: Int,
        private val itemSelectionKey: String
) : ItemDetailsLookup.ItemDetails<String>() {

    override fun getSelectionKey() = itemSelectionKey

    override fun getPosition() = itemPosition
}

class TorrentKeyProvider(private val torrentAdapter: TorrentListAdapter)
    : ItemKeyProvider<String>(SCOPE_MAPPED) {

    override fun getKey(position: Int) = torrentAdapter.torrentList[position].hash

    override fun getPosition(key: String) = torrentAdapter.torrentList.indexOfFirst { it.hash == key }
}

class TorrentDetailsLookup(private val recyclerView: RecyclerView)
    : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view) as? TorrentListAdapter.TorrentViewHolder
        return holder?.getItemDetails()
    }
}
