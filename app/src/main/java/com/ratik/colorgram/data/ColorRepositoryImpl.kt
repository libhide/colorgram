package com.ratik.colorgram.data

import com.ratik.colorgram.*
import com.ratik.colorgram.model.GramColor

class ColorRepositoryImpl(private val localStorage: LocalStorage) : ColorRepository {

    override fun saveColor(color: GramColor) {
        localStorage.putInt(PREF_RED, color.red)
        localStorage.putInt(PREF_GREEN, color.green)
        localStorage.putInt(PREF_BLUE, color.blue)
    }

    override fun getColor(): GramColor {
        val red = localStorage.getInt(PREF_RED, APP_RED)
        val green = localStorage.getInt(PREF_GREEN, APP_GREEN)
        val blue = localStorage.getInt(PREF_BLUE, APP_BLUE)
        return GramColor(red, green, blue)
    }
}