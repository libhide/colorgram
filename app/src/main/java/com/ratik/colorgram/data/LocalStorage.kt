package com.ratik.colorgram.data

interface LocalStorage {
    fun putInt(key: String, value: Int)
    fun getInt(key: String, default: Int): Int
}
