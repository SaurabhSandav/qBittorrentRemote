package com.redridgeapps.remoteforqbittorrent.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.MultiChoiceListener
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment

val BaseFragment.compatActivity
    get() = requireActivity() as AppCompatActivity

fun <T : ViewModel> BaseFragment.getViewModel(viewModelClass: Class<T>): T {
    return ViewModelProviders.of(this, viewModelFactory).get(viewModelClass)
}

fun <T> LiveData<T>.asMutable(): MutableLiveData<T> {
    return this as MutableLiveData<T>
}

fun Long.humanReadableByteCount(isSpeed: Boolean = false, si: Boolean = false): String {
    val bytes = this
    val unit = if (si) 1000 else 1024
    val speed = if (isSpeed) "/s" else ""

    if (bytes < unit) return bytes.toString() + " B" + speed

    val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
    val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"

    return String.format(
            "%.1f %sB%s",
            bytes / Math.pow(unit.toDouble(), exp.toDouble()),
            pre,
            speed
    )
}

fun <T : ViewDataBinding> ViewGroup.recyclerDataBindingInflate(
        @LayoutRes layoutRes: Int,
        parent: ViewGroup = this,
        attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutRes,
            parent,
            attachToParent
    )
}

fun MaterialDialog.listItemsMultiChoiceCustom(
        items: List<String>,
        initialSelection: List<String>,
        selection: MultiChoiceListener
) = listItemsMultiChoice(
        items = items,
        initialSelection = initialSelection.map { items.indexOf(it) }.toIntArray(),
        selection = selection
)
