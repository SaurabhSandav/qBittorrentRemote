package com.redridgeapps.remoteforqbittorrent.repo

import android.content.SharedPreferences
import android.content.res.Resources
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(
        res: Resources,
        prefs: SharedPreferences
)
