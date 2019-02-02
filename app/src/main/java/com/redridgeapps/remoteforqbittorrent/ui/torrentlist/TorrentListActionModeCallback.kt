package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import com.redridgeapps.remoteforqbittorrent.R

class TorrentListActionModeCallback(
        private val titleGenerator: () -> String,
        private val actionCallback: (Action, ActionMode) -> Unit,
        private val onDestroy: () -> Unit
) : ActionMode.Callback {

    enum class Action {
        SELECT_ALL,
        SELECT_INVERSE,
        PAUSE,
        RESUME,
        RECHECK,
        DELETE
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        val action = when (item?.itemId) {
            R.id.action_select_all -> Action.SELECT_ALL
            R.id.action_select_inverse -> Action.SELECT_INVERSE
            R.id.action_pause -> Action.PAUSE
            R.id.action_resume -> Action.RESUME
            R.id.action_recheck -> Action.RECHECK
            R.id.action_delete -> Action.DELETE
            else -> return false
        }

        actionCallback(action, mode!!)

        return true
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.selection_menu, menu)
        mode?.menuInflater?.inflate(R.menu.torrentlist_selection_menu, menu)

        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.title = titleGenerator()
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) = onDestroy()
}
