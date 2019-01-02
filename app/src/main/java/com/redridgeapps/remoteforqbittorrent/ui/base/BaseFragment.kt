package com.redridgeapps.remoteforqbittorrent.ui.base

import androidx.fragment.app.Fragment
import arrow.core.Either
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener
import com.redridgeapps.remoteforqbittorrent.R
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface BaseFragmentMarker

abstract class BaseFragment : Fragment(), BaseFragmentMarker {

    protected fun showError(error: Either<Int, String>) {
        when (error) {
            is Either.Left -> Snackbar.make(requireView(), error.a, Snackbar.LENGTH_SHORT).show()
            is Either.Right -> Snackbar.make(requireView(), error.b, Snackbar.LENGTH_SHORT).show()
        }
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
