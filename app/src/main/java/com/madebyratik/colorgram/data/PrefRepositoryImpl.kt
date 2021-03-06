package com.madebyratik.colorgram.data

import com.madebyratik.colorgram.FIRST_RUN

class PrefRepositoryImpl(private val localStorage: LocalStorage) : PrefRepository {

    override fun firstRunDone() {
        localStorage.putInt(FIRST_RUN, 0)
    }

    override fun isFirstRun(): Boolean {
        return localStorage.getInt(FIRST_RUN, 1) == 1
    }
}