package com.example.gallery.presentation.common

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gallery.presentation.details.SlideshowViewModel
import com.example.gallery.presentation.gallery.GalleryViewModel

class GalleryViewModelFactory(private val contentResolver: ContentResolver) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SlideshowViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SlideshowViewModel() as T
        } else if (modelClass.isAssignableFrom(GalleryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(contentResolver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}