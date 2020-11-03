package com.madebyratik.colorgram

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.madebyratik.colorgram.data.*
import com.madebyratik.colorgram.ui.main.DownloadHelper
import com.madebyratik.colorgram.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidContext()) }

    single { DownloadHelper(androidContext()) }

    single<LocalStorage> { SharedPrefs(get()) }
    single<ColorRepository>{ ColorRepositoryImpl(get()) }
    single<PrefRepository>{ PrefRepositoryImpl(get()) }

    viewModel { MainViewModel(get(), get()) }
}