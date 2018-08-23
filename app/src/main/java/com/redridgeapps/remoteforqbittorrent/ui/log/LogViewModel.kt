package com.redridgeapps.remoteforqbittorrent.ui.log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.model.QBittorrentLog
import com.redridgeapps.remoteforqbittorrent.model.ResIdMapper
import com.redridgeapps.remoteforqbittorrent.repo.PreferenceRepository
import com.redridgeapps.remoteforqbittorrent.repo.QBittorrentRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseViewModel
import com.redridgeapps.remoteforqbittorrent.ui.log.model.FilterState
import com.redridgeapps.remoteforqbittorrent.ui.log.model.LogListItem
import com.redridgeapps.remoteforqbittorrent.util.asMutable
import kotlinx.coroutines.experimental.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class LogViewModel @Inject constructor(
        private val qBitRepo: QBittorrentRepository,
        private val prefRepo: PreferenceRepository
) : BaseViewModel() {

    var sortLatest by Delegates.observable(prefRepo.logListSort) { _, _, newValue ->
        prefRepo.logListSort = newValue
        updateList()
        logListLiveData.asMutable().postValue(Try.just(Unit))
    }

    var filterState: FilterState by Delegates.observable(FilterState()) { _, _, _ ->
        lastId = -1
        logList = ArrayList()
        refreshLogList()
    }

    var logList: List<LogListItem> = ArrayList()
    val logListLiveData: LiveData<Try<Unit>> = MutableLiveData()

    private var lastId = -1

    init {
        refreshLogList()
    }

    fun refreshLogList() = launch {
        val result = qBitRepo.getLog(
                normal = filterState.normal,
                info = filterState.info,
                warning = filterState.warning,
                critical = filterState.critical,
                lastId = lastId
        ).map(::updateList)

        logListLiveData.asMutable().postValue(result)
    }

    private fun updateList(newLogs: List<QBittorrentLog>? = null) {
        if (newLogs?.isNotEmpty() == true) lastId = newLogs.last().id

        logList = ArrayList(logList).let { list ->
            if (newLogs != null) list.addAll(newLogs.mapToLogItem())

            if (sortLatest) list.sortedByDescending { it.id }
            else list.sortedBy { it.id }
        }
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