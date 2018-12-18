package com.redridgeapps.remoteforqbittorrent.ui.log

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.ItemLogListBinding
import com.redridgeapps.remoteforqbittorrent.ui.log.model.LogListItem
import com.redridgeapps.remoteforqbittorrent.util.recyclerDataBindingInflate
import javax.inject.Inject

class LogListAdapter @Inject constructor() :
        ListAdapter<LogListItem, LogListAdapter.ViewHolder>(LogListItem.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLogListBinding = parent.recyclerDataBindingInflate(R.layout.item_log_list)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemLogListBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(log: LogListItem) {
            binding.log = log
            binding.executePendingBindings()
        }
    }
}
