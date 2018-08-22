package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentTorrentListBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.util.getViewModel
import javax.inject.Inject

class TorrentListFragment : BaseFragment() {

    @Inject
    lateinit var torrentListAdapter: TorrentListAdapter

    private lateinit var binding: FragmentTorrentListBinding
    private lateinit var viewModel: TorrentListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(TorrentListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_torrent_list, container, false)

        binding.srl.setOnRefreshListener { viewModel.refreshTorrentList() }
        binding.srl.isRefreshing = true

        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        observeTorrentList()
    }

    private fun observeTorrentList() {
        viewModel.torrentListLiveData.observe(this, Observer { result ->
            result.fold({ showError(R.string.error_generic) }, torrentListAdapter::submitList)

            binding.srl.isRefreshing = false
        })
    }

    private fun setupRecyclerView() = binding.recyclerView.run {
        val linearLayoutManager = LinearLayoutManager(requireContext())

        setHasFixedSize(true)
        adapter = torrentListAdapter
        layoutManager = linearLayoutManager
        addItemDecoration(DividerItemDecoration(requireContext(), linearLayoutManager.orientation))
    }
}
