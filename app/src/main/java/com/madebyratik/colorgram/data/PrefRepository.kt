package com.madebyratik.colorgram.data

interface PrefRepository {
    fun firstRunDone()
    fun isFirstRun(): Boolean
}