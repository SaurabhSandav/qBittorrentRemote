package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import android.Manifest
import android.content.ClipboardManager
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.appcompat.view.ActionMode
import androidx.core.content.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import arrow.core.Either
import arrow.core.Failure
import arrow.core.Success
import arrow.core.toOption
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.checkbox.checkBoxPrompt
import com.afollestad.materialdialogs.checkbox.isCheckPromptChecked
import com.afollestad.materialdialogs.files.FileFilter
import com.afollestad.materialdialogs.files.fileChooser
import com.afollestad.materialdialogs.files.folderChooser
import com.afollestad.materialdialogs.input.input
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService.Sort
import com.redridgeapps.remoteforqbittorrent.databinding.FragmentTorrentListBinding
import com.redridgeapps.remoteforqbittorrent.ui.MainViewModel
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragment
import com.redridgeapps.remoteforqbittorrent.ui.base.showError
import com.redridgeapps.remoteforqbittorrent.ui.base.withPermissions
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.TorrentListActionModeCallback.Action
import com.redridgeapps.remoteforqbittorrent.util.MIME_TYPE_TORRENT_FILE
import com.redridgeapps.remoteforqbittorrent.util.compatActivity
import java.io.File
import javax.inject.Inject

class TorrentListFragment @Inject constructor(
        val torrentListAdapter: TorrentListAdapter,
        viewModelFactory: ViewModelProvider.Factory
) : BaseFragment() {

    private lateinit var binding: FragmentTorrentListBinding
    private lateinit var selectionTracker: SelectionTracker<String>
    private var actionMode: ActionMode? = null
    private val viewModel by viewModels<TorrentListViewModel> { viewModelFactory }
    private val activityViewModel by activityViewModels<MainViewModel>()

    private val fileFilter: (File) -> Boolean = {
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(it.extension) == MIME_TYPE_TORRENT_FILE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_torrent_list, container, false)

        binding.srl.setOnRefreshListener { viewModel.refreshTorrentList() }
        binding.srl.isRefreshing = true

        setupRecyclerView(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeActivity()
        observeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectionTracker.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.torrentlist_options_menu, menu)

        val id = when (viewModel.listSort) {
            Sort.NAME -> R.id.action_sort_name
            Sort.SIZE -> R.id.action_sort_size
            Sort.ETA -> R.id.action_sort_eta
            Sort.PRIORITY -> R.id.action_sort_priority
            Sort.PROGRESS -> R.id.action_sort_progress
            Sort.RATIO -> R.id.action_sort_ratio
            Sort.STATE -> R.id.action_sort_state
            Sort.DLSPEED -> R.id.action_sort_download_speed
            Sort.UPSPEED -> R.id.action_sort_upload_speed
            else -> throw IllegalStateException("Unknown argument: ${viewModel.listSort}")
        }

        menu.findItem(id).isChecked = true
        menu.findItem(R.id.action_sort_reverse).isChecked = viewModel.listSortReverse
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = true

        when (item.itemId) {
            R.id.action_add_link -> addTorrentLink()
            R.id.action_add_file -> addTorrentFile()
            R.id.action_add_folder -> addTorrentFilesFromFolder()
            R.id.action_sort_name -> setSort(item, Sort.NAME)
            R.id.action_sort_size -> setSort(item, Sort.SIZE)
            R.id.action_sort_eta -> setSort(item, Sort.ETA)
            R.id.action_sort_priority -> setSort(item, Sort.PRIORITY)
            R.id.action_sort_progress -> setSort(item, Sort.PROGRESS)
            R.id.action_sort_ratio -> setSort(item, Sort.RATIO)
            R.id.action_sort_state -> setSort(item, Sort.STATE)
            R.id.action_sort_download_speed -> setSort(item, Sort.DLSPEED)
            R.id.action_sort_upload_speed -> setSort(item, Sort.UPSPEED)
            R.id.action_sort_reverse -> setSortReverse(item)
            R.id.action_pause_all -> viewModel.pause()
            R.id.action_resume_all -> viewModel.resume()
            else -> result = super.onOptionsItemSelected(item)
        }

        return result
    }

    private fun observeActivity() {
        activityViewModel.filterLiveData.observe(viewLifecycleOwner) {
            viewModel.filter = it
        }
    }

    private fun observeViewModel() {
        observeGenericOperations()
        observeTorrentList()
    }

    private fun observeGenericOperations() {
        viewModel.genericOpResultLiveData.observe(viewLifecycleOwner) { result ->
            if (result is Failure) showError(Either.left(R.string.error_generic))
        }
    }

    private fun observeTorrentList() {
        viewModel.torrentListLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Success -> torrentListAdapter.submitList(result.value)
                is Failure -> showError(Either.left(R.string.error_generic))
            }

            binding.srl.isRefreshing = false
        }
    }

    private fun setupRecyclerView(savedInstanceState: Bundle?) = binding.recyclerView.run {
        val linearLayoutManager = LinearLayoutManager(requireContext())

        setHasFixedSize(true)
        adapter = torrentListAdapter
        layoutManager = linearLayoutManager
        addItemDecoration(DividerItemDecoration(requireContext(), linearLayoutManager.orientation))

        selectionTracker = torrentListAdapter.setupSelectionTracker(this, SelectionObserver())
        selectionTracker.onRestoreInstanceState(savedInstanceState)

        if (selectionTracker.selection.size() > 0)
            actionMode = startTorrentListActionMode()
    }

    private fun setSort(item: MenuItem, sort: String): Boolean {
        item.isChecked = true
        viewModel.listSort = sort
        return true
    }

    private fun setSortReverse(item: MenuItem): Boolean {
        item.isChecked = !item.isChecked
        viewModel.listSortReverse = item.isChecked
        return true
    }

    private fun addTorrentLink() {
        val prefill = getLinkFromClipboard()
        MaterialDialog(requireContext())
                .title(R.string.torrentlist_dialog_label_add_link)
                .input(
                        hint = "http://, https://, magnet: or bc://bt/",
                        prefill = prefill,
                        inputType = InputType.TYPE_CLASS_TEXT
                ) { _, text -> viewModel.addTorrentLinks(listOf(text.toString())) }
                .positiveButton(android.R.string.ok)
                .negativeButton(android.R.string.cancel)
                .show()
    }

    private fun getLinkFromClipboard(): String? {
        return requireActivity().getSystemService<ClipboardManager>()
                .toOption()
                .filter { it.hasPrimaryClip() }
                .flatMap { it.primaryClip.toOption() }
                .map { it.getItemAt(0).coerceToText(requireContext()).toString() }
                .orNull()
                ?.takeIf {
                    listSupportedLinks
                            .any { protocol -> it.startsWith(protocol) } || it.matches(Regex(INFO_HASH_PATTERN))
                }
    }

    private fun addTorrentFile() {
        val permissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        withPermissions(permissions, R.string.error_need_read_storage_permission) {

            val filter: FileFilter = { it.isDirectory || fileFilter(it) }

            MaterialDialog(requireContext())
                    .fileChooser(filter = filter) { _, file -> viewModel.addTorrentFiles(listOf(file)) }
                    .negativeButton(android.R.string.cancel)
                    .show()
        }
    }

    private fun addTorrentFilesFromFolder() {
        val permissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        withPermissions(permissions, R.string.error_need_read_storage_permission) {

            MaterialDialog(requireContext())
                    .folderChooser { _, file ->
                        val fileList = file.listFiles().toList().filter(fileFilter)
                        viewModel.addTorrentFiles(fileList)
                    }
                    .negativeButton(android.R.string.cancel)
                    .show()
        }
    }

    private fun startTorrentListActionMode(): ActionMode? {

        val actionCallback = { action: Action, actionMode: ActionMode ->

            fun finishMode(operation: () -> Unit) {
                operation()
                actionMode.finish()
            }

            when (action) {
                Action.SELECT_ALL -> torrentListAdapter.selectAll()
                Action.SELECT_INVERSE -> torrentListAdapter.selectInverse()
                Action.PAUSE -> finishMode { viewModel.pause(selectionTracker.selection.toList()) }
                Action.RESUME -> finishMode { viewModel.resume(selectionTracker.selection.toList()) }
                Action.RECHECK -> finishMode { viewModel.recheck(selectionTracker.selection.toList()) }
                Action.DELETE -> deleteTorrents(actionMode)
            }
        }

        return compatActivity.startSupportActionMode(TorrentListActionModeCallback(
                titleGenerator = { selectionTracker.selection.size().toString() },
                actionCallback = actionCallback,
                onDestroy = {
                    selectionTracker.clearSelection()
                    actionMode = null
                }
        ))
    }

    private fun deleteTorrents(mode: ActionMode?) {
        MaterialDialog(requireContext())
                .title(R.string.torrentlist_dialog_label_are_you_sure)
                .checkBoxPrompt(R.string.torrentlist_dialog_label_delete_with_data) {}
                .positiveButton(R.string.torrentlist_selection_delete) { dialog ->
                    val isChecked = dialog.isCheckPromptChecked()
                    viewModel.delete(isChecked, selectionTracker.selection.toList())

                    mode?.finish()
                }
                .negativeButton(R.string.label_cancel)
                .show()
    }

    private inner class SelectionObserver : SelectionTracker.SelectionObserver<String>() {
        override fun onSelectionChanged() {
            super.onSelectionChanged()
            val selectionSize = selectionTracker.selection.size()

            when {
                selectionSize <= 0 -> actionMode?.finish()
                actionMode == null -> actionMode = startTorrentListActionMode()
                else -> actionMode?.invalidate()
            }
        }
    }
}

private const val INFO_HASH_PATTERN = """\b[0-9a-fA-F]{40}\b"""
private val listSupportedLinks = listOf("http://", "https://", "magnet:", "bc://bt/")
