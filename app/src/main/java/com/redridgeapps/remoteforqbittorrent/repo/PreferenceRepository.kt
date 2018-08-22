package com.redridgeapps.remoteforqbittorrent.repo

import android.content.SharedPreferences
import android.content.res.Resources
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService.Companion.DEFAULT_BASE_URL
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService.Companion.DEFAULT_PASSWORD
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService.Companion.DEFAULT_USERNAME
import com.redridgeapps.remoteforqbittorrent.util.delegate.BooleanPrefDelegate
import com.redridgeapps.remoteforqbittorrent.util.delegate.StringPrefDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(
        res: Resources,
        prefs: SharedPreferences
) {

    var initialConfigFinished by BooleanPrefDelegate(
            prefs,
            res.getString(R.string.key_pref_initial_config_finished),
            DEFAULT_INITIAL_CONFIG_FINISHED
    )

    var baseUrl by StringPrefDelegate(
            prefs,
            res.getString(R.string.key_pref_base_url),
            DEFAULT_BASE_URL
    )

    var username by StringPrefDelegate(
            prefs,
            res.getString(R.string.key_pref_username),
            DEFAULT_USERNAME
    )

    var password by StringPrefDelegate(
            prefs,
            res.getString(R.string.key_pref_password),
            DEFAULT_PASSWORD
    )

    var sid by StringPrefDelegate(
            prefs,
            res.getString(R.string.key_pref_sid),
            EMPTY_STR
    )

    var torrentListSort by StringPrefDelegate(
            prefs,
            res.getString(R.string.key_pref_torrent_list_sort),
            DEFAULT_TORRENT_LIST_SORT
    )

    var torrentListSortReverse by BooleanPrefDelegate(
            prefs,
            res.getString(R.string.key_pref_torrent_list_sort_reverse),
            DEFAULT_TORRENT_LIST_SORT_REVERSE
    )
}

private const val EMPTY_STR = ""
private const val DEFAULT_INITIAL_CONFIG_FINISHED = false
private const val DEFAULT_TORRENT_LIST_SORT = QBittorrentService.Sort.PRIORITY
private const val DEFAULT_TORRENT_LIST_SORT_REVERSE = false
