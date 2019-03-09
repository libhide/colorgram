package com.ratik.colorgram.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratik.colorgram.data.ColorRepository
import com.ratik.colorgram.model.GramColor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(private val colorRepository: ColorRepository) : ViewModel() {
    var selectedColor = MutableLiveData<GramColor>()
    var slidersAreVisible = false

    init {
        GlobalScope.launch { selectedColor.postValue(colorRepository.getColor()) }
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
        GlobalScope.launch { colorRepository.saveColor(selectedColor.value!!) }
    }
}