package com.redridgeapps.remoteforqbittorrent.ui.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.redridgeapps.remoteforqbittorrent.R
import dagger.android.support.AndroidSupportInjection

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.prefs)
    }
}
