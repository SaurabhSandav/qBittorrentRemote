package com.redridgeapps.remoteforqbittorrent.ui.config

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentConfigBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.util.compatActivity
import com.redridgeapps.remoteforqbittorrent.util.getViewModel

class ConfigFragment : BaseFragment() {

    private lateinit var binding: FragmentConfigBinding
    private lateinit var viewModel: ConfigViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        compatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(ConfigViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_config, container, false)
        return binding.root
    }
}