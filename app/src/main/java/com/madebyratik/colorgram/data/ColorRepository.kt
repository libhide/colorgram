package com.madebyratik.colorgram.data

import com.madebyratik.colorgram.model.GramColor
import kotlinx.coroutines.Deferred

interface ColorRepository {
    suspend fun saveColor(color: GramColor): Deferred<Unit>
    suspend fun getColor(): Deferred<GramColor>
}