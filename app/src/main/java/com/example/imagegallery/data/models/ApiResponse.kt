package com.example.imagegallery.data.models


import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("photos")
    val photos: Photos,
    @SerializedName("stat")
    val stat: String
)