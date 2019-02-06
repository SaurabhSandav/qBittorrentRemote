package com.redridgeapps.remoteforqbittorrent.ui.torrentdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentTorrentDetailsBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.util.dataBindingInflate
import javax.inject.Inject

class TorrentDetailsFragment @Inject constructor(
        viewModelFactory: ViewModelProvider.Factory
) : BaseFragment() {

    private lateinit var binding: FragmentTorrentDetailsBinding
    private val viewModel by viewModels<TorrentDetailsViewModel> { viewModelFactory }
    private val args by navArgs<TorrentDetailsFragmentArgs>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = inflater.dataBindingInflate(R.layout.fragment_torrent_details, container)

        return binding.root
    }
}