package com.redridgeapps.remoteforqbittorrent.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.MultiChoiceListener
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentLogBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.ui.log.model.FilterState
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
            R.id.item_filter -> setFilter()
            else -> result = super.onOptionsItemSelected(item)
        }

        return result
    }

    private fun observeViewModel() {
        observeLogList()
    }

    private fun observeLogList() {
        viewModel.logListLiveData.observe(this) { result ->
            result.fold({ showError(R.string.error_generic) }, { logListAdapter.submitList(viewModel.logList) })

            binding.srl.isRefreshing = false
        }
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

    private fun setFilter() {

        val filterItems = ArrayList<String>(4)
        filterItems.add(LABEL_LOG_TYPE_NORMAL_INDEX, getString(R.string.log_type_normal))
        filterItems.add(LABEL_LOG_TYPE_INFO_INDEX, getString(R.string.log_type_info))
        filterItems.add(LABEL_LOG_TYPE_WARNING_INDEX, getString(R.string.log_type_warning))
        filterItems.add(LABEL_LOG_TYPE_CRITICAL_INDEX, getString(R.string.log_type_critical))

        val selectedIndices = ArrayList<Int>()

        viewModel.run {
            if (filterState.normal) selectedIndices.add(LABEL_LOG_TYPE_NORMAL_INDEX)
            if (filterState.info) selectedIndices.add(LABEL_LOG_TYPE_INFO_INDEX)
            if (filterState.warning) selectedIndices.add(LABEL_LOG_TYPE_WARNING_INDEX)
            if (filterState.critical) selectedIndices.add(LABEL_LOG_TYPE_CRITICAL_INDEX)
        }

        val itemsCallbackMultiChoice: MultiChoiceListener = { _, indices, _ ->
            viewModel.filterState = FilterState(
                    normal = indices.contains(LABEL_LOG_TYPE_NORMAL_INDEX),
                    info = indices.contains(LABEL_LOG_TYPE_INFO_INDEX),
                    warning = indices.contains(LABEL_LOG_TYPE_WARNING_INDEX),
                    critical = indices.contains(LABEL_LOG_TYPE_CRITICAL_INDEX)
            )
        }

        MaterialDialog(requireContext())
                .title(R.string.log_menu_label_filter)
                .listItemsMultiChoice(
                        items = filterItems,
                        initialSelection = selectedIndices.toIntArray(),
                        selection = itemsCallbackMultiChoice
                )
                .positiveButton(android.R.string.ok)
                .negativeButton(android.R.string.cancel)
                .show()
    }
}

const val LABEL_LOG_TYPE_NORMAL_INDEX = 0
const val LABEL_LOG_TYPE_INFO_INDEX = 1
const val LABEL_LOG_TYPE_WARNING_INDEX = 2
const val LABEL_LOG_TYPE_CRITICAL_INDEX = 3
