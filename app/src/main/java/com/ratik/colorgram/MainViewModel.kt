package com.ratik.colorgram

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratik.colorgram.data.ColorRepository
import com.ratik.colorgram.model.GramColor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel(private val colorRepository: ColorRepository) : ViewModel() {
    var selectedColor = MutableLiveData<GramColor>()

    init {
        GlobalScope.launch { selectedColor.postValue(colorRepository.getColor()) }
    }

    fun observeSelectedColor(): LiveData<GramColor> {
        return selectedColor
    }

    fun saveColor() {
        GlobalScope.launch { colorRepository.saveColor(selectedColor.value!!) }
    }
}