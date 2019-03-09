package com.ratik.colorgram.ui.model

import org.junit.Assert
import org.junit.Test

class GramColorTest {

    @Test
    fun testOverlayColorChoice() {
        val testColors = listOf(
                GramColor(),
                GramColor(255, 0, 0),
                GramColor(0,0,0),
                GramColor(255, 255, 255)
        )

        Assert.assertEquals(true, testColors[0].shouldOverlayColorBeWhite())
        Assert.assertEquals(true, testColors[1].shouldOverlayColorBeWhite())
        Assert.assertEquals(true, testColors[2].shouldOverlayColorBeWhite())
        Assert.assertEquals(false, testColors[3].shouldOverlayColorBeWhite())
    }
}