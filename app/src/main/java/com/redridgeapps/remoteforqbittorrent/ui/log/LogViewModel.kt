package com.redridgeapps.remoteforqbittorrent.ui.log

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import arrow.core.Success
import arrow.core.Try
import com.redridgeapps.remoteforqbittorrent.model.QBittorrentLog
import com.redridgeapps.remoteforqbittorrent.model.ResIdMapper
import com.redridgeapps.remoteforqbittorrent.repo.PreferenceRepository
import com.redridgeapps.remoteforqbittorrent.repo.QBittorrentRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseViewModel
import com.redridgeapps.remoteforqbittorrent.ui.log.model.FilterState
import com.redridgeapps.remoteforqbittorrent.ui.log.model.LogListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class LogViewModel @Inject constructor(
        private val qBitRepo: QBittorrentRepository,
        private val prefRepo: PreferenceRepository
) : BaseViewModel() {

    private val _logListLiveData = MutableLiveData<Try<Unit>>()

    var sortLatest: Boolean by Delegates.observable(prefRepo.logListSort) { _, _, newValue ->
        prefRepo.logListSort = newValue
        viewModelScope.launch { updateList() }
    }

    var filterState: FilterState by Delegates.observable(FilterState()) { _, _, _ ->
        lastId = -1
        logList = ArrayList()
        refreshLogList()
    }

    var logList: List<LogListItem> = ArrayList()
    val logListLiveData: LiveData<Try<Unit>> = _logListLiveData

    private var lastId = -1

    init {
        refreshLogList()
    }

    fun refreshLogList() = viewModelScope.launch {
        val result = qBitRepo.getLog(
                normal = filterState.normal,
                info = filterState.info,
                warning = filterState.warning,
                critical = filterState.critical,
                lastId = lastId
        )

        if (result is Success) updateList(result.value)
    }

    private suspend fun updateList(
            newLogs: List<QBittorrentLog>? = null
    ) = withContext(Dispatchers.IO) {

        newLogs?.lastOrNull()?.let { lastId = it.id }

        // Create new List
        val newList = ArrayList(logList)

        // Map new logs to new List
        newLogs?.mapTo(newList, this@LogViewModel::toLogItem)

        // Sort new list
        if (sortLatest) newList.sortByDescending { it.id }
        else newList.sortBy { it.id }

        // Assign it to class property
        logList = newList

        _logListLiveData.postValue(Try.just(Unit))
    }

    private fun toLogItem(log: QBittorrentLog): LogListItem {
        val type = with(ResIdMapper) { log.type.toResId() }
        val date = Date(log.timestamp)
        val timestamp = SimpleDateFormat.getDateTimeInstance().format(date)

        return LogListItem(
                id = log.id,
                message = log.message,
                timestamp = timestamp,
                type = type
        )
    }
}
