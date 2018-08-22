package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import android.os.Bundle
import android.view.*
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
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.torrentlist_options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = true

        when (item.itemId) {
            R.id.action_pause_all -> viewModel.pauseAll()
            R.id.action_resume_all -> viewModel.resumeAll()
            else -> result = super.onOptionsItemSelected(item)
        }

        return result
    }

    private fun observeViewModel() {
        observeGenericOperations()
        observeTorrentList()
    }

    private fun observeGenericOperations() {
        viewModel.genericOpResultLiveData.observe(this, Observer { result ->
            result.fold({ showError(R.string.error_generic) }, {})
        })
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
