package com.madebyratik.colorgram

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.madebyratik.colorgram.data.ColorRepository
import com.madebyratik.colorgram.data.ColorRepositoryImpl
import com.madebyratik.colorgram.data.LocalStorage
import com.madebyratik.colorgram.data.PrefRepository
import com.madebyratik.colorgram.data.PrefRepositoryImpl
import com.madebyratik.colorgram.data.SharedPrefs
import com.madebyratik.colorgram.ui.main.DownloadHelper
import com.madebyratik.colorgram.ui.main.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(androidContext()) }

    single { DownloadHelper(androidContext()) }

    single<LocalStorage> { SharedPrefs(get()) }
    single<ColorRepository> { ColorRepositoryImpl(get()) }
    single<PrefRepository> { PrefRepositoryImpl(get()) }

    viewModel { MainViewModel(get(), get()) }
}