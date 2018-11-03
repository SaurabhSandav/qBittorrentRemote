package com.redridgeapps.remoteforqbittorrent.ui.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentConfigBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.util.compatActivity
import com.redridgeapps.remoteforqbittorrent.util.getViewModel

class ConfigFragment : BaseFragment() {

    private lateinit var binding: FragmentConfigBinding
    private lateinit var viewModel: ConfigViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel(ConfigViewModel::class.java)
        compatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_config, container, false)
        binding.btLetsGo.setOnClickListener { letsGoClicked() }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLogin()
    }

    private fun observeLogin() {
        viewModel.loginResultLiveData.observe(this) { result ->
            result.fold({ showError(text = it.message) }, { launchMainActivity() })
        }
    }

    private fun launchMainActivity() = findNavController().run {
        setGraph(R.navigation.nav_graph)
        navigate(ConfigFragmentDirections.actionConfigFragmentToTorrentListFragment())
    }

    private fun letsGoClicked() = viewModel.login(
            host = binding.tilHost.editText?.text.toString(),
            port = binding.tilPort.editText?.text.toString().toInt(),
            useHttps = binding.swUseHttps.isChecked,
            username = binding.tilUsername.editText?.text.toString(),
            password = binding.tilPassword.editText?.text.toString()
    )
}
