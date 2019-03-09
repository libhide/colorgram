package com.ratik.colorgram.data

import com.ratik.colorgram.model.GramColor

interface ColorRepository {
    suspend fun saveColor(color: GramColor)
    suspend fun getColor(): GramColor
}