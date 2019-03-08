package com.ratik.colorgram.model

import com.ratik.colorgram.APP_BLUE
import com.ratik.colorgram.APP_GREEN
import com.ratik.colorgram.APP_RED

data class GramColor(
        val red: Int = APP_RED,
        val green: Int = APP_GREEN,
        val blue: Int = APP_BLUE
) {

    // Logic: https://stackoverflow.com/a/9780689/3150771
    fun shouldOverlayColorBeWhite(): Boolean {
        val y = 0.2126 * red + 0.7152 * green + 0.0722 * blue
        return y < 200
    }
}