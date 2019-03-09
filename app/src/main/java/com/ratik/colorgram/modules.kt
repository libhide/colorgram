package com.ratik.colorgram

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ratik.colorgram.data.ColorRepository
import com.ratik.colorgram.data.ColorRepositoryImpl
import com.ratik.colorgram.data.LocalStorage
import com.ratik.colorgram.data.SharedPrefs
import com.ratik.colorgram.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidContext()) }

    single<LocalStorage> { SharedPrefs(get()) }
    single<ColorRepository>{ ColorRepositoryImpl(get()) }

    viewModel { MainViewModel(get()) }
}