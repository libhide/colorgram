package com.madebyratik.colorgram.data

import com.madebyratik.colorgram.model.GramColor

interface ColorRepository {
    suspend fun saveColor(color: GramColor)
    suspend fun getColor(): GramColor
}
