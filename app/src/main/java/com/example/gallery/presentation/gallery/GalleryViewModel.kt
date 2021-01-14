package com.example.gallery.presentation.gallery

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gallery.core.data.Image
import com.example.gallery.core.data.repository.Repository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class GalleryViewModel(contentResolver: ContentResolver) : ViewModel() {
    private var _imagePaths: MutableLiveData<List<Image>> = MutableLiveData()
    var imagePaths = _imagePaths as LiveData<List<Image>>

    private var _eventProvider: MutableLiveData<Event> = MutableLiveData()
    var eventProvider = _eventProvider as LiveData<Event>

    private var repository = Repository()

    init {
        viewModelScope.launch {
            _imagePaths.value = repository.getImages(contentResolver)
        }
    }

    fun onItemClick(position: Int) {
        _eventProvider.value = Event.ShowDetails(position)
    }

    sealed class Event {
        data class ShowDetails(val position: Int) : Event()
    }
}