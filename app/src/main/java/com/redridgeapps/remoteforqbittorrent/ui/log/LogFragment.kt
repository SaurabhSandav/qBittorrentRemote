package com.redridgeapps.remoteforqbittorrent.ui.log

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentLogBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.util.getViewModel
import javax.inject.Inject

class LogFragment : BaseFragment() {

    @Inject
    lateinit var logListAdapter: LogListAdapter

    private lateinit var binding: FragmentLogBinding
    private lateinit var viewModel: LogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = getViewModel(LogViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log, container, false)

        binding.srl.setOnRefreshListener { viewModel.refreshLogList() }
        binding.srl.isRefreshing = true

        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.log_options_menu, menu)

        val id = if (viewModel.sortLatest) R.id.item_sort_latest else R.id.item_sort_oldest
        menu?.findItem(id)?.isChecked = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = true

        when (item.itemId) {
            R.id.item_sort_latest -> setSort(item, true)
            R.id.item_sort_oldest -> setSort(item, false)
            else -> result = super.onOptionsItemSelected(item)
        }

        return result
    }

    private fun observeViewModel() {
        observeLogList()
    }

    private fun observeLogList() {
        viewModel.logListLiveData.observe(this, Observer { result ->
            result.fold({ showError(R.string.error_generic) }, { logListAdapter.submitList(viewModel.logList) })

            binding.srl.isRefreshing = false
        })
    }

    private fun setupRecyclerView() = binding.recyclerView.run {
        val linearLayoutManager = LinearLayoutManager(requireContext())

        setHasFixedSize(true)
        adapter = logListAdapter
        layoutManager = linearLayoutManager
        addItemDecoration(DividerItemDecoration(requireContext(), linearLayoutManager.orientation))
    }

    private fun setSort(item: MenuItem, latest: Boolean) {
        item.isChecked = true
        viewModel.sortLatest = latest
    }
}
