package com.example.imagegallery.utils

import androidx.annotation.StringRes

sealed class NetworkResults<T>(
    val data: T? = null,
    @StringRes val messageResId: Int? = null,
    val isErrorHandled: Boolean? = null
) {
    class Success<T>(data: T) : NetworkResults<T>(data)
    class Error<T>(@StringRes messageResId: Int, isHandled: Boolean = false) :
        NetworkResults<T>(null, messageResId, isHandled)

    class Loading<T> : NetworkResults<T>()
}
