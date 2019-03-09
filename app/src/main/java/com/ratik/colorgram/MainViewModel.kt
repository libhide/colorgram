package com.ratik.colorgram

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratik.colorgram.model.GramColor

class MainViewModel : ViewModel() {
    var selectedColor = MutableLiveData<GramColor>()

    init {
        selectedColor.value = GramColor()
    }

    fun observeSelectedColor(): LiveData<GramColor> {
        return selectedColor
    }
}