package com.redridgeapps.remoteforqbittorrent.ui.common

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

val Fragment.compatActivity
    get() = requireActivity() as AppCompatActivity

fun <T : ViewDataBinding> LayoutInflater.dataBindingInflate(
        @LayoutRes resource: Int,
        root: ViewGroup?,
        attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(this, resource, root, attachToParent)
}

fun <T : ViewDataBinding> ViewGroup.recyclerDataBindingInflate(
        @LayoutRes resource: Int,
        attachToParent: Boolean = false
): T {
    return LayoutInflater.from(context).dataBindingInflate(resource, this, attachToParent)
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
