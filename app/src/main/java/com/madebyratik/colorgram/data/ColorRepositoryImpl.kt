package com.madebyratik.colorgram.data

import com.madebyratik.colorgram.*
import com.madebyratik.colorgram.model.GramColor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class ColorRepositoryImpl(private val localStorage: LocalStorage) : ColorRepository {

    override suspend fun saveColor(color: GramColor): Deferred<Unit> {
        return GlobalScope.async {
            localStorage.putInt(PREF_RED, color.red)
            localStorage.putInt(PREF_GREEN, color.green)
            localStorage.putInt(PREF_BLUE, color.blue)
        }
    }

    override suspend fun getColor(): Deferred<GramColor> {
        return GlobalScope.async {
            val red = localStorage.getInt(PREF_RED, APP_RED)
            val green = localStorage.getInt(PREF_GREEN, APP_GREEN)
            val blue = localStorage.getInt(PREF_BLUE, APP_BLUE)
            GramColor(red, green, blue)
        }
    }
}