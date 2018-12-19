package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.model.ResIdMapper
import com.redridgeapps.remoteforqbittorrent.model.Torrent
import com.redridgeapps.remoteforqbittorrent.repo.PreferenceRepository
import com.redridgeapps.remoteforqbittorrent.repo.QBittorrentRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseViewModel
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.model.TorrentListItem
import com.redridgeapps.remoteforqbittorrent.util.humanReadableByteCount
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject
import kotlin.properties.Delegates

class TorrentListViewModel @Inject constructor(
        private val qBitRepo: QBittorrentRepository,
        private val prefRepo: PreferenceRepository
) : BaseViewModel() {

    var filter: String? by Delegates.observable(null) { _, _, _ -> refreshTorrentList() }

    var listSort by Delegates.observable(prefRepo.torrentListSort) { _, _, newValue ->
        prefRepo.torrentListSort = newValue
        refreshTorrentList()
    }

    var listSortReverse by Delegates.observable(prefRepo.torrentListSortReverse) { _, _, newValue ->
        prefRepo.torrentListSortReverse = newValue
        refreshTorrentList()
    }

    private val _genericOpResultLiveData = MutableLiveData<Try<Unit>>()
    private val _torrentListLiveData = MutableLiveData<Try<List<TorrentListItem>>>()

    val genericOpResultLiveData: LiveData<Try<Unit>> = _genericOpResultLiveData
    val torrentListLiveData: LiveData<Try<List<TorrentListItem>>> = _torrentListLiveData

    init {
        refreshTorrentList()
    }

    fun refreshTorrentList() = viewModelScope.launch {
        val result = qBitRepo.getTorrentList(filter).map { it.mapToTorrentListItem() }
        _torrentListLiveData.value = result
    }

    fun pause(hashes: List<String>? = null) = viewModelScope.launch {
        val result = qBitRepo.pause(hashes)
        _genericOpResultLiveData.value = result
    }

    fun resume(hashes: List<String>? = null) = viewModelScope.launch {
        val result = qBitRepo.resume(hashes)
        _genericOpResultLiveData.value = result
    }

    fun delete(
            deleteFiles: Boolean,
            hashes: List<String>? = null
    ) = viewModelScope.launch {
        val result = qBitRepo.delete(deleteFiles, hashes)
        _genericOpResultLiveData.value = result
    }

    fun recheck(hashes: List<String>? = null) = viewModelScope.launch {
        val result = qBitRepo.recheck(hashes)
        _genericOpResultLiveData.value = result
    }

    fun addTorrentLinks(links: List<String>) = viewModelScope.launch {
        val result = qBitRepo.addTorrentLinks(links)
        _genericOpResultLiveData.value = result
    }

    fun addTorrentFiles(file: List<File>) = viewModelScope.launch {
        val result = qBitRepo.addTorrentFiles(file)
        _genericOpResultLiveData.value = result
    }

    private fun List<Torrent>.mapToTorrentListItem() = map {
        val progress = it.progress * PERCENT_UNIT

        TorrentListItem(
                hash = it.hash,
                title = it.name,
                progress = progress.toInt(),
                progressStr = String.format("%.2f%%", progress),
                state = it.state,
                statusResId = with(ResIdMapper) { it.state.toResId() },
                dlSpeed = it.dlSpeed.humanReadableByteCount(isSpeed = true),
                upSpeed = it.upSpeed.humanReadableByteCount(isSpeed = true)
        )
    }
}

private const val PERCENT_UNIT = 100
