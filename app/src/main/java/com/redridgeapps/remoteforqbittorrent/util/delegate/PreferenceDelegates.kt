package com.redridgeapps.remoteforqbittorrent.util.delegate

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringPrefDelegate(
        private val prefs: SharedPreferences,
        private val prefKey: String,
        private val defaultValue: String
) : ReadWriteProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return prefs.getString(prefKey, defaultValue)!!
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        prefs.edit { putString(prefKey, value) }
    }
}

class IntPrefDelegate(
        private val prefs: SharedPreferences,
        private val prefKey: String,
        private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return prefs.getInt(prefKey, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        prefs.edit { putInt(prefKey, value) }
    }
}

class BooleanPrefDelegate(
        private val prefs: SharedPreferences,
        private val prefKey: String,
        private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return prefs.getBoolean(prefKey, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        prefs.edit { putBoolean(prefKey, value) }
    }
}
