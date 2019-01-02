package com.redridgeapps.remoteforqbittorrent.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.ui.base.BaseFragmentMarker
import javax.inject.Inject

class SettingsFragment @Inject constructor() : PreferenceFragmentCompat(), BaseFragmentMarker {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }
}
