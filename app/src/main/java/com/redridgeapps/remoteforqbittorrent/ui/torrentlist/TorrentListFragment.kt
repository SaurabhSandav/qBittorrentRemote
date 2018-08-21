package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentTorrentListBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.util.getViewModel

class TorrentListFragment : BaseFragment() {

    private lateinit var binding: FragmentTorrentListBinding
    private lateinit var viewModel: TorrentListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(TorrentListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_torrent_list, container, false)
        return binding.root
    }
}
