package com.redridgeapps.remoteforqbittorrent.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
}
