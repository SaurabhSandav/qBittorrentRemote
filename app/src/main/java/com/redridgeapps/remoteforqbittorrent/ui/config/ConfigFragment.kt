package com.redridgeapps.remoteforqbittorrent.ui.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import arrow.core.Success
import arrow.core.left
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentConfigBinding
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.showError
import com.redridgeapps.remoteforqbittorrent.ui.common.compatActivity
import com.redridgeapps.remoteforqbittorrent.ui.common.dataBindingInflate
import javax.inject.Inject

class ConfigFragment @Inject constructor(
        viewModelFactory: ViewModelProvider.Factory
) : BaseFragment() {

    private lateinit var binding: FragmentConfigBinding
    private val viewModel by viewModels<ConfigViewModel> { viewModelFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        compatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)

        if (viewModel.isInitialConfigFinished()) {
            launchTorrentListScreen()
            return null
        }

        binding = inflater.dataBindingInflate(R.layout.fragment_config, container)
        binding.btLetsGo.setOnClickListener { letsGoClicked() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLogin()
    }

    private fun observeLogin() {
        viewModel.loginResultLiveData.observe(viewLifecycleOwner) { result ->
            when {
                result is Success && result.value -> launchTorrentListScreen()
                else -> showError(R.string.error_generic.left())
            }
        }
    }

    private fun launchTorrentListScreen() {
        findNavController().navigate(ConfigFragmentDirections.toTorrentListScreen())
    }

    private fun letsGoClicked() = viewModel.login(
            host = binding.tilHost.editText?.text.toString(),
            port = binding.tilPort.editText?.text.toString().toInt(),
            useHttps = binding.swUseHttps.isChecked,
            username = binding.tilUsername.editText?.text.toString(),
            password = binding.tilPassword.editText?.text.toString()
    )
}
