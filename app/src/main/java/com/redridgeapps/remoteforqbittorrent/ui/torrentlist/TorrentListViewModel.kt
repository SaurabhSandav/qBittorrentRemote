package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.model.ResIdMapper
import com.redridgeapps.remoteforqbittorrent.model.Torrent
import com.redridgeapps.remoteforqbittorrent.repo.PreferenceRepository
import com.redridgeapps.remoteforqbittorrent.repo.QBittorrentRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseViewModel
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.model.TorrentListItem
import com.redridgeapps.remoteforqbittorrent.util.asMutable
import com.redridgeapps.remoteforqbittorrent.util.humanReadableByteCount
import kotlinx.coroutines.experimental.launch
import java.io.File
import javax.inject.Inject
import kotlin.properties.Delegates

class TorrentListViewModel @Inject constructor(
        private val qBitRepo: QBittorrentRepository,
        private val prefRepo: PreferenceRepository
) : BaseViewModel() {

    var filter: String? by Delegates.observable<String?>(null) { _, _, _ -> refreshTorrentList() }

    var listSort by Delegates.observable(prefRepo.torrentListSort) { _, _, newValue ->
        prefRepo.torrentListSort = newValue
        refreshTorrentList()
    }

    var listSortReverse by Delegates.observable(prefRepo.torrentListSortReverse) { _, _, newValue ->
        prefRepo.torrentListSortReverse = newValue
        refreshTorrentList()
    }

    val genericOpResultLiveData: LiveData<Try<Unit>> = MutableLiveData()
    val torrentListLiveData: LiveData<Try<List<TorrentListItem>>> = MutableLiveData()

    init {
        refreshTorrentList()
    }

    fun refreshTorrentList() = launch {
        val result = qBitRepo.getTorrentList(filter).map { it.mapToTorrentListItem() }
        torrentListLiveData.asMutable().postValue(result)
    }

    fun pause(hashes: List<String>? = null) = launch {
        val result = qBitRepo.pause(hashes)
        genericOpResultLiveData.asMutable().postValue(result)
    }

    fun resume(hashes: List<String>? = null) = launch {
        val result = qBitRepo.resume(hashes)
        genericOpResultLiveData.asMutable().postValue(result)
    }

    fun addTorrentLinks(links: List<String>) = launch {
        val result = qBitRepo.addTorrentLinks(links)
        genericOpResultLiveData.asMutable().postValue(result)
    }

    fun addTorrentFiles(file: List<File>) = launch {
        val result = qBitRepo.addTorrentFiles(file)
        genericOpResultLiveData.asMutable().postValue(result)
    }

    private fun List<Torrent>.mapToTorrentListItem() = map {
        val progress = it.progress * 100

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
