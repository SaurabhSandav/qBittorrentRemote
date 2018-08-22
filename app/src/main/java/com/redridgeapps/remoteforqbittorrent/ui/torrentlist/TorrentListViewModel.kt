package com.redridgeapps.remoteforqbittorrent.ui.torrentlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.model.ResIdMapper
import com.redridgeapps.remoteforqbittorrent.model.Torrent
import com.redridgeapps.remoteforqbittorrent.repo.QBittorrentRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseViewModel
import com.redridgeapps.remoteforqbittorrent.ui.torrentlist.model.TorrentListItem
import com.redridgeapps.remoteforqbittorrent.util.asMutable
import com.redridgeapps.remoteforqbittorrent.util.humanReadableByteCount
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class TorrentListViewModel @Inject constructor(
        private val qBitRepo: QBittorrentRepository
) : BaseViewModel() {

    val genericOpResultLiveData: LiveData<Try<Unit>> = MutableLiveData()
    val torrentListLiveData: LiveData<Try<List<TorrentListItem>>> = MutableLiveData()

    init {
        refreshTorrentList()
    }

    fun refreshTorrentList() = launch {
        val result = qBitRepo.getTorrentList().map { it.mapToTorrentListItem() }
        torrentListLiveData.asMutable().postValue(result)
    }

    fun pauseAll() = launch {
        val result = qBitRepo.pause()
        genericOpResultLiveData.asMutable().postValue(result)
    }

    fun resumeAll() = launch {
        val result = qBitRepo.resume()
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
