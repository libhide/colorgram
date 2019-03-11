package com.ratik.colorgram.data

import com.ratik.colorgram.model.GramColor
import kotlinx.coroutines.Deferred

interface ColorRepository {
    suspend fun saveColor(color: GramColor): Deferred<Unit>
    suspend fun getColor(): Deferred<GramColor>
}