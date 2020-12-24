package com.example.gallery.presentation.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gallery.core.data.Image

class SlideshowViewModel : ViewModel() {
    private var _imagePaths: MutableLiveData<List<Image>> = MutableLiveData()
    var imagePaths = _imagePaths as LiveData<List<Image>>

    private var _selectedPosition: MutableLiveData<Int> = MutableLiveData()
    //var selectedPosition = _selectedPosition

    fun setup(imagePaths: List<Image>, selectedPosition: Int) {
        _imagePaths.value = imagePaths
        _selectedPosition.value = selectedPosition
    }

}