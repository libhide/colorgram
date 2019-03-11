package com.ratik.colorgram.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratik.colorgram.data.ColorRepository
import com.ratik.colorgram.model.GramColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val colorRepository: ColorRepository,
                    private val downloadHelper: DownloadHelper) : ViewModel() {
    private val viewModelJob = Job()
    private val backgroundScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    var selectedColor = MutableLiveData<GramColor>()
    var slidersAreVisible = false

    init {
        backgroundScope.launch {
            val color = colorRepository.getColor().await()
            selectedColor.postValue(color)
        }
    }

    fun selectedColor(): LiveData<GramColor> {
        return selectedColor
    }

    fun setRed(red: Int) {
        selectedColor.value = GramColor(red, selectedColor.value!!.green, selectedColor.value!!.blue)
    }

    fun setGreen(green: Int) {
        selectedColor.value = GramColor(selectedColor.value!!.red, green, selectedColor.value!!.blue)
    }

    fun setBlue(blue: Int) {
        selectedColor.value = GramColor(selectedColor.value!!.red, selectedColor.value!!.green, blue)
    }

    fun saveColor() {
        backgroundScope.launch {
            colorRepository.saveColor(selectedColor.value!!).await()
        }
    }

    fun downloadColor() {
        backgroundScope.launch {
            val downloadedFile = downloadHelper.downloadColor(selectedColor.value!!).await()
            downloadHelper.broadcastSaveIntent(downloadedFile)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}