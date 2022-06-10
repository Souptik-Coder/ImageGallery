package com.example.imagegallery.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagegallery.R
import com.example.imagegallery.adapters.PhotoLoadStateAdapter
import com.example.imagegallery.adapters.PhotoPagingAdapter
import com.example.imagegallery.databinding.FragmentHomeBinding
import com.example.imagegallery.viewmodels.MainViewModel

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val photosAdapter by lazy { PhotoPagingAdapter() }
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        setUpRecyclerView()
        binding.retryBtn.setOnClickListener {
            photosAdapter.retry()
        }
        viewModel.photos.observe(viewLifecycleOwner) { pagingData ->
            photosAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }
        photosAdapter.addLoadStateListener { loadStates ->
            binding.progressBar.isVisible = loadStates.source.refresh is LoadState.Loading
            binding.errorTextView.isVisible = loadStates.source.refresh is LoadState.Error
            binding.retryBtn.isVisible = loadStates.source.refresh is LoadState.Error
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            val photoLayoutManager = GridLayoutManager(context, 2)
            photoLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (position == photosAdapter.itemCount) 2 else 1

            }
            layoutManager = photoLayoutManager
            adapter = photosAdapter.withLoadStateHeaderAndFooter(
                header = PhotoLoadStateAdapter { photosAdapter.retry() },
                footer = PhotoLoadStateAdapter { photosAdapter.retry() }
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}