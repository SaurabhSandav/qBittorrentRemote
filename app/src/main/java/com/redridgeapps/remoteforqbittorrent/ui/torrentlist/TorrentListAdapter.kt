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
import com.redridgeapps.remoteforqbittorrent.ui.common.SelectionTrackerExtra
import com.redridgeapps.remoteforqbittorrent.ui.common.withExtras
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.TorrentListAdapter.TorrentViewHolder
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.model.TorrentListItem
import com.redridgeapps.remoteforqbittorrent.util.lazyUnsynchronized
import com.redridgeapps.remoteforqbittorrent.util.recyclerDataBindingInflate
import javax.inject.Inject

class TorrentListAdapter(
        recyclerView: RecyclerView
) : ListAdapter<TorrentListItem, TorrentViewHolder>(TorrentListItem.DiffCallback) {

    class Factory @Inject constructor() {
        fun create(recyclerView: RecyclerView) = TorrentListAdapter(recyclerView)
    }

    var torrentList: List<TorrentListItem> = ArrayList()
    val selectionTrackerExtra: SelectionTrackerExtra<String> by lazyUnsynchronized {

        val torrentKeyProvider = TorrentKeyProvider(
                keyFromPositionGenerator = { torrentList[it].hash },
                positionFromKeyGenerator = { key -> torrentList.indexOfFirst { it.hash == key } }
        )

        SelectionTracker.Builder(
                javaClass.simpleName,
                recyclerView,
                torrentKeyProvider,
                TorrentDetailsLookup(recyclerView),
                StorageStrategy.createStringStorage()
        ).build().withExtras { torrentList.map { it.hash } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentViewHolder {
        val binding: ListItemTorrentBinding = parent.recyclerDataBindingInflate(R.layout.list_item_torrent)
        return TorrentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TorrentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, selectionTrackerExtra.isSelected(item.hash))
    }

    override fun submitList(list: List<TorrentListItem>?) {
        super.submitList(list)
        torrentList = list!!
    }

    inner class TorrentViewHolder(
            private val binding: ListItemTorrentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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

class TorrentKeyProvider(
        private val keyFromPositionGenerator: (Int) -> String,
        private val positionFromKeyGenerator: (String) -> Int
) : ItemKeyProvider<String>(SCOPE_MAPPED) {

    override fun getKey(position: Int) = keyFromPositionGenerator(position)

    override fun getPosition(key: String) = positionFromKeyGenerator(key)
}

class TorrentDetailsLookup(
        private val recyclerView: RecyclerView
) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y) ?: return null
        val holder = recyclerView.getChildViewHolder(view) as? TorrentViewHolder
        return holder?.getItemDetails()
    }
}
