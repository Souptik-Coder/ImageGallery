package com.example.imagegallery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagegallery.data.models.Photo
import com.example.imagegallery.databinding.PhotoItemBinding

class PhotosAdapter : RecyclerView.Adapter<PhotosAdapter.ViewHolder>() {
    private var photos: List<Photo> = emptyList()

    inner class ViewHolder(val binding: PhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.apply {
                textView.text = photo.title
                Glide.with(binding.root).load(photo.urlS).into(binding.imageView)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            PhotoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount(): Int = photos.size

    fun setData(photos: List<Photo>) {
        this.photos = photos
        notifyDataSetChanged() // setData Only called once after api fetch so no effect on performance
    }

}