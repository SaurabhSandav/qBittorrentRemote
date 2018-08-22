package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService.Sort
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentTorrentListBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.DrawerActivityContract
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
        observeActivity()
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.torrentlist_options_menu, menu)

        val id = when (viewModel.listSort) {
            Sort.NAME -> R.id.action_sort_name
            Sort.SIZE -> R.id.action_sort_size
            Sort.ETA -> R.id.action_sort_eta
            Sort.PRIORITY -> R.id.action_sort_priority
            Sort.PROGRESS -> R.id.action_sort_progress
            Sort.RATIO -> R.id.action_sort_ratio
            Sort.STATE -> R.id.action_sort_state
            Sort.DLSPEED -> R.id.action_sort_download_speed
            Sort.UPSPEED -> R.id.action_sort_upload_speed
            else -> throw IllegalStateException("Unknown argument: ${viewModel.listSort}")
        }

        menu?.findItem(id)?.isChecked = true
        menu?.findItem(R.id.action_sort_reverse)?.isChecked = viewModel.listSortReverse
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = true

        when (item.itemId) {
            R.id.action_sort_name -> setSort(item, Sort.NAME)
            R.id.action_sort_size -> setSort(item, Sort.SIZE)
            R.id.action_sort_eta -> setSort(item, Sort.ETA)
            R.id.action_sort_priority -> setSort(item, Sort.PRIORITY)
            R.id.action_sort_progress -> setSort(item, Sort.PROGRESS)
            R.id.action_sort_ratio -> setSort(item, Sort.RATIO)
            R.id.action_sort_state -> setSort(item, Sort.STATE)
            R.id.action_sort_download_speed -> setSort(item, Sort.DLSPEED)
            R.id.action_sort_upload_speed -> setSort(item, Sort.UPSPEED)
            R.id.action_sort_reverse -> setSortReverse(item)
            R.id.action_pause_all -> viewModel.pauseAll()
            R.id.action_resume_all -> viewModel.resumeAll()
            else -> result = super.onOptionsItemSelected(item)
        }

        return result
    }

    private fun observeActivity() {
        val contractActivity = activity as? DrawerActivityContract ?: return

        contractActivity.navigationItemSelectionsLiveData.observe(this, Observer {
            viewModel.filter = it
        })
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

    private fun setSort(item: MenuItem, sort: String): Boolean {
        item.isChecked = true
        viewModel.listSort = sort
        return true
    }

    private fun setSortReverse(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked
        viewModel.listSortReverse = item.isChecked
        return true
    }
}
