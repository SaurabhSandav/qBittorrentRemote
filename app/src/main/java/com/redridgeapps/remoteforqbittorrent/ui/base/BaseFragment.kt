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

abstract class BaseFragment : Fragment()

fun Fragment.showError(error: Either<Int, String>) {
    when (error) {
        is Either.Left -> Snackbar.make(requireView(), error.a, Snackbar.LENGTH_SHORT).show()
        is Either.Right -> Snackbar.make(requireView(), error.b, Snackbar.LENGTH_SHORT).show()
    }
}

fun Fragment.withPermissions(
        permissions: List<String>,
        errResId: Int = -1,
        onSuccess: () -> Unit
) {

    val permissionBuilder = Dexter.withActivity(requireActivity()).withPermissions(permissions)

    val listener = object : BaseMultiplePermissionsListener() {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            super.onPermissionsChecked(report)

            if (report.areAllPermissionsGranted()) onSuccess()
        }
    }

    CompositeMultiplePermissionsListener(listener)

    val compositeListener = if (errResId == -1) {
        listener
    } else {
        val snackbarListener = SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                .with(view, errResId)
                .withOpenSettingsButton(R.string.permission_label_settings)
                .build()

        CompositeMultiplePermissionsListener(listener, snackbarListener)
    }

    permissionBuilder.withListener(compositeListener).check()
}
