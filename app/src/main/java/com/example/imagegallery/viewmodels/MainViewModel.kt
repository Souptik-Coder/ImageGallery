package com.example.imagegallery.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.imagegallery.data.Repository
import com.example.imagegallery.data.models.Photo
import com.example.imagegallery.utils.NetworkResults
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _searchPhotoResponse: MutableLiveData<NetworkResults<List<Photo>>> =
        MutableLiveData()
    val searchPhotoResponse: LiveData<NetworkResults<List<Photo>>> = _searchPhotoResponse

    val photos = repository.getPhotos().cachedIn(viewModelScope)

    private var searchJob: Job? = null
    private var currentQuery = ""

    fun searchPhotoDebounced(query: String) {
        searchJob?.cancel()
        currentQuery = query
        searchJob = viewModelScope.launch {
            delay(600)
            _searchPhotoResponse.value = NetworkResults.Loading()
            _searchPhotoResponse.value = repository.searchPhotos(query)
        }
    }

    fun setErrorHandled() {
       _searchPhotoResponse.value = null
    }

    fun retrySearchPhoto() {
        searchPhotoDebounced(currentQuery)
    }
}