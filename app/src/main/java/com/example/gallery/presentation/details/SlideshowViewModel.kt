package com.example.gallery.presentation.details

import androidx.lifecycle.ViewModel
import com.example.gallery.core.data.Image

class SlideshowViewModel : ViewModel() {
    var imagePaths: List<Image> = ArrayList()

    fun getImage(position: Int): Image {
        return imagePaths[position]
    }
}