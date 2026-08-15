package com.madebyratik.colorgram.data

import com.madebyratik.colorgram.APP_BLUE
import com.madebyratik.colorgram.APP_GREEN
import com.madebyratik.colorgram.APP_RED
import com.madebyratik.colorgram.PREF_BLUE
import com.madebyratik.colorgram.PREF_GREEN
import com.madebyratik.colorgram.PREF_RED
import com.madebyratik.colorgram.model.GramColor

class ColorRepositoryImpl(private val localStorage: LocalStorage) : ColorRepository {
    override suspend fun saveColor(color: GramColor) {
        localStorage.putInt(PREF_RED, color.red)
        localStorage.putInt(PREF_GREEN, color.green)
        localStorage.putInt(PREF_BLUE, color.blue)
    }

    override suspend fun getColor(): GramColor {
        val red = localStorage.getInt(PREF_RED, APP_RED)
        val green = localStorage.getInt(PREF_GREEN, APP_GREEN)
        val blue = localStorage.getInt(PREF_BLUE, APP_BLUE)
        return GramColor(red, green, blue)
    }
}
