package com.example.imagegallery.data

import com.example.imagegallery.data.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    companion object {
        const val BASE_URL = "https://api.flickr.com/"
    }

    @GET("services/rest/?method=flickr.photos.getRecent&api_key=6f102c62f41998d151e5a1b48713cf13&format=json&nojsoncallback=1&extras=url_s")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): Response<ApiResponse>
}