package com.redridgeapps.remoteforqbittorrent.ui.base

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    protected fun showError(@StringRes resId: Int? = null, text: String? = null) {
        resId?.let { Snackbar.make(view!!, it, Snackbar.LENGTH_SHORT).show() }
        text?.let { Snackbar.make(view!!, it, Snackbar.LENGTH_SHORT).show() }
    }
}
