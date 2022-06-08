package com.example.imagegallery.data

import android.util.Log
import com.example.imagegallery.R
import com.example.imagegallery.data.models.Photo
import com.example.imagegallery.utils.NetworkResults
import retrofit2.HttpException
import java.io.IOException

class Repository(private val apiInterface: ApiInterface) {
    suspend fun getPhotos(): NetworkResults<List<Photo>> {
        Log.e("Repo","Api calling")
        return try {
            val response = apiInterface.getPhotos()
            val photos = response.body()?.photos?.photo
            if (response.isSuccessful && !photos.isNullOrEmpty()) {
                NetworkResults.Success(photos)
            } else {
                NetworkResults.Error(R.string.unknown_error)
            }

        }catch (e: IOException) {
            NetworkResults.Error(R.string.internet_error)
        } catch (e: Exception) {
            NetworkResults.Error(R.string.unknown_error)
        }
    }
}