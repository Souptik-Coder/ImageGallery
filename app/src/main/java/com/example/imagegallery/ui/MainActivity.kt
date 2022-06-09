package com.example.imagegallery.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagegallery.R
import com.example.imagegallery.adapters.PhotoLoadStateAdapter
import com.example.imagegallery.adapters.PhotoPagingAdapter
import com.example.imagegallery.databinding.ActivityMainBinding
import com.example.imagegallery.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val photosAdapter by lazy { PhotoPagingAdapter() }
    private val viewModel: MainViewModel by viewModels()
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true);

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

        binding.retryBtn.setOnClickListener {
            photosAdapter.retry()
        }
        viewModel.photos.observe(this) { pagingData ->
            photosAdapter.submitData(lifecycle, pagingData)
        }
        photosAdapter.addLoadStateListener { loadStates ->
            binding.progressBar.isVisible = loadStates.source.refresh is LoadState.Loading
            binding.errorTextView.isVisible = loadStates.source.refresh is LoadState.Error
            binding.retryBtn.isVisible = loadStates.source.refresh is LoadState.Error
        }
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}