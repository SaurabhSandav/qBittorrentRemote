package com.redridgeapps.remoteforqbittorrent.ui.log

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentLogBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.util.getViewModel

class LogFragment : BaseFragment() {

    private lateinit var binding: FragmentLogBinding
    private lateinit var viewModel: LogViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(LogViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_log, container, false)
        return binding.root
    }
}
