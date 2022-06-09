package com.example.imagegallery.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData

class Repository(private val apiInterface: ApiInterface) {
    fun getPhotos() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PhotoPagingSource(apiInterface) }
        ).liveData
}