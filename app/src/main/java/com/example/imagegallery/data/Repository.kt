package com.example.imagegallery.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.imagegallery.R
import com.example.imagegallery.data.models.Photo
import com.example.imagegallery.utils.NetworkResults
import java.io.IOException

class Repository(private val apiInterface: ApiInterface) {
    fun getPhotos() =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false,
                prefetchDistance = 5
            ),
            pagingSourceFactory = { PhotoPagingSource(apiInterface) }
        ).liveData

    suspend fun searchPhotos(query: String): NetworkResults<List<Photo>> {
        Log.e("Repo", "Search Api calling")
        return try {
            val response = apiInterface.searchPhoto(query)
            val photos = response.body()?.photos?.photo
            if (response.isSuccessful && !photos.isNullOrEmpty()) {
                NetworkResults.Success(photos)
            } else {
                NetworkResults.Error(R.string.unknown_error)
            }

        } catch (e: IOException) {
            NetworkResults.Error(R.string.internet_error)
        } catch (e: Exception) {
            NetworkResults.Error(R.string.unknown_error)
        }
    }
}