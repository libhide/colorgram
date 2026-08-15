package com.madebyratik.colorgram.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madebyratik.colorgram.data.ColorRepository
import com.madebyratik.colorgram.model.GramColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val colorRepository: ColorRepository,
    private val downloadHelper: DownloadHelper,
) : ViewModel() {
    val selectedColor = MutableLiveData<GramColor>()
    var slidersAreVisible = false

    init {
        viewModelScope.launch(Dispatchers.IO) {
            selectedColor.postValue(colorRepository.getColor())
        }
    }

    fun setRed(red: Int) {
        selectedColor.value?.let { selectedColor.value = GramColor(red, it.green, it.blue) }
    }

    fun setGreen(green: Int) {
        selectedColor.value?.let { selectedColor.value = GramColor(it.red, green, it.blue) }
    }

    fun setBlue(blue: Int) {
        selectedColor.value?.let { selectedColor.value = GramColor(it.red, it.green, blue) }
    }

    fun saveColor() {
        val color = selectedColor.value ?: return
        viewModelScope.launch(Dispatchers.IO) {
            colorRepository.saveColor(color)
        }
    }

    fun downloadColor() {
        val color = selectedColor.value ?: return
        viewModelScope.launch {
            downloadHelper.downloadColor(color)
        }
    }
}
