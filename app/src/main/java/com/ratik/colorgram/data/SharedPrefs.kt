package com.ratik.colorgram.data

import android.content.SharedPreferences

class SharedPrefs(val prefs: SharedPreferences) : LocalStorage {

    override fun putInt(key: String, value: Int) {
        with(prefs.edit()) {
            this.putInt(key, value)
            apply()
        }
    }

    override fun getInt(key: String, default: Int): Int {
        return prefs.getInt(key, default)
    }
}