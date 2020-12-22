package com.example.gallery.presentation.gallery

import android.content.ContentResolver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gallery.core.data.Image
import com.example.gallery.core.data.repository.Repository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GalleryViewModel(contentResolver: ContentResolver) : ViewModel() {
    var imagePaths: MutableLiveData<List<Image>> = MutableLiveData()
    private var repository = Repository()

    init {
        viewModelScope.launch {
            imagePaths.value = repository.getImages(contentResolver)
        }
    }

    fun getImages() = imagePaths
}