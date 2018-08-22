package com.redridgeapps.remoteforqbittorrent.ui.log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.model.QBittorrentLog
import com.redridgeapps.remoteforqbittorrent.model.ResIdMapper
import com.redridgeapps.remoteforqbittorrent.repo.QBittorrentRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseViewModel
import com.redridgeapps.remoteforqbittorrent.ui.log.model.LogListItem
import com.redridgeapps.remoteforqbittorrent.util.asMutable
import kotlinx.coroutines.experimental.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class LogViewModel @Inject constructor(
        private val qBitRepo: QBittorrentRepository
) : BaseViewModel() {

    var logList: List<LogListItem> = ArrayList()
    val logListLiveData: LiveData<Try<Unit>> = MutableLiveData()

    private var lastId = -1

    init {
        refreshLogList()
    }

    fun refreshLogList() = launch {
        val result = qBitRepo.getLog(lastId = lastId).map(this@LogViewModel::updateList)

        logListLiveData.asMutable().postValue(result)
    }

    private fun updateList(newLogs: List<QBittorrentLog>) {
        if (newLogs.isNotEmpty()) lastId = newLogs.last().id

        logList = ArrayList(logList).apply { addAll(newLogs.mapToLogItem()) }
    }

    private fun List<QBittorrentLog>.mapToLogItem() = map {
        val type = with(ResIdMapper) { it.type.toResId() }
        val date = Date(it.timestamp)
        val timestamp = SimpleDateFormat.getDateTimeInstance().format(date)

        LogListItem(
                id = it.id,
                message = it.message,
                timestamp = timestamp,
                type = type
        )
    }
}