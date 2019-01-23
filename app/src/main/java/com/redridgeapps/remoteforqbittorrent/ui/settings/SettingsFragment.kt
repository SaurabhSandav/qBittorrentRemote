package com.redridgeapps.remoteforqbittorrent.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.redridgeapps.remoteforqbittorrent.R
import javax.inject.Inject

class SettingsFragment @Inject constructor() : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }
}
