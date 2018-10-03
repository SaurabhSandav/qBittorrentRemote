package com.redridgeapps.remoteforqbittorrent.ui.base

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.redridgeapps.remoteforqbittorrent.R
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import kotlin.coroutines.experimental.suspendCoroutine

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

    protected suspend fun askPermissions(
            errResId: Int, permissions: List<String>
    ) = suspendCoroutine<List<String>> { continuation ->

        val snackbarListener = SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                .with(view, errResId)
                .withOpenSettingsButton(R.string.permission_label_settings)
                .build()

        val listener = object : BaseMultiplePermissionsListener() {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                super.onPermissionsChecked(report)

                if (report == null) return

                continuation.resume(report.grantedPermissionResponses.map { it.permissionName })
            }
        }

        Dexter.withActivity(requireActivity())
                .withPermissions(permissions)
                .withListener(CompositeMultiplePermissionsListener(listener, snackbarListener))
                .check()
    }
}
