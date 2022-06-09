package com.example.imagegallery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.imagegallery.databinding.LoadingStateLayoutBinding

class PhotoLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PhotoLoadStateAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: LoadingStateLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryBtn.setOnClickListener {
                retry.invoke()
            }
        }
        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                textView.isVisible = loadState is LoadState.Error
                retryBtn.isVisible = loadState is LoadState.Error
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        return ViewHolder(
            LoadingStateLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}