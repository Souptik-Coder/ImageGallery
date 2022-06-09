package com.example.imagegallery.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagegallery.data.models.Photo

class PhotoPagingSource(
    private val apiInterface: ApiInterface
) : PagingSource<Int, Photo>() {
    private val START_PAGE_INDEX = 1
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val position = params.key ?: START_PAGE_INDEX
            Log.e("PagingSource","Api calling for pos $position")
            val response = apiInterface.getPhotos(position)
            val photos = response.body()?.photos!!
            LoadResult.Page(
                data = photos.photo,
                prevKey = if (position == START_PAGE_INDEX) null else position - 1,
                nextKey = if (position < photos.pages) position + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}