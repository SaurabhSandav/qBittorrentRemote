package com.redridgeapps.remoteforqbittorrent.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.MultiChoiceListener
import com.afollestad.materialdialogs.list.listItemsMultiChoice

private const val SI_UNITS = 1000
private const val BINARY_UNITS = 1024

val Fragment.compatActivity
    get() = requireActivity() as AppCompatActivity

fun Long.humanReadableByteCount(isSpeed: Boolean = false, si: Boolean = false): String {
    val bytes = this
    val unit = if (si) SI_UNITS else BINARY_UNITS
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
