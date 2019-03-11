package com.ratik.colorgram

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.ratik.colorgram.data.*
import com.ratik.colorgram.ui.main.DownloadHelper
import com.ratik.colorgram.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidContext()) }

    single { DownloadHelper(androidContext()) }

    single<LocalStorage> { SharedPrefs(get()) }
    single<ColorRepository>{ ColorRepositoryImpl(get()) }
    single<PrefRepository>{ PrefRepositoryImpl(get()) }

    viewModel { MainViewModel(get()) }
}