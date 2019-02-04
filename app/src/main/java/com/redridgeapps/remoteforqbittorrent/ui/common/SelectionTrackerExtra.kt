package com.redridgeapps.remoteforqbittorrent.ui.common

import android.os.Bundle
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver

class SelectionTrackerExtra<K>(
        private val selectionTracker: SelectionTracker<K>,
        private val keyListGenerator: () -> List<K>
) {

    val selection: Selection<K> get() = selectionTracker.selection

    fun addObserver(observer: SelectionObserver<K>) = selectionTracker.addObserver(observer)

    fun isSelected(key: K?) = selectionTracker.isSelected(key)

    fun clearSelection() = selectionTracker.clearSelection()

    fun setItemsSelected(keys: Iterable<K>, selected: Boolean) = selectionTracker.setItemsSelected(keys, selected)

    fun onSaveInstanceState(state: Bundle) = selectionTracker.onSaveInstanceState(state)

    fun onRestoreInstanceState(state: Bundle?) = selectionTracker.onRestoreInstanceState(state)
}

fun <K> SelectionTracker<K>.withExtras(keyListGenerator: () -> List<K>): SelectionTrackerExtra<K> {
    return SelectionTrackerExtra(this, keyListGenerator)
}