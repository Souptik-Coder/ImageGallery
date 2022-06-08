package com.example.imagegallery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagegallery.data.Repository
import com.example.imagegallery.data.models.Photo
import com.example.imagegallery.utils.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _photoResponse: MutableLiveData<NetworkResults<List<Photo>>> = MutableLiveData()
    val photoResponse: LiveData<NetworkResults<List<Photo>>> = _photoResponse

    init {
        getPhotos()
    }

    private fun getPhotos() = viewModelScope.launch {
        _photoResponse.value = NetworkResults.Loading()
        _photoResponse.value = repository.getPhotos()
    }

    fun retryLoadPhotos() {
        getPhotos()
    }
}