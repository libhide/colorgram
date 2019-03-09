package com.ratik.colorgram.data

interface PrefRepository {
    fun firstRunDone()
    fun isFirstRun(): Boolean
}