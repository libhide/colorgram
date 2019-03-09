package com.ratik.colorgram.data

import com.ratik.colorgram.ui.model.GramColor

interface ColorRepository {
    fun saveColor(color: GramColor)
    fun getColor(): GramColor
}