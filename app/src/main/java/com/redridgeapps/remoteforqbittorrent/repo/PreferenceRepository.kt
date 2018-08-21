package com.redridgeapps.remoteforqbittorrent.repo

import android.content.SharedPreferences
import android.content.res.Resources
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.util.delegate.BooleanPrefDelegate
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
}

private const val DEFAULT_INITIAL_CONFIG_FINISHED = false
