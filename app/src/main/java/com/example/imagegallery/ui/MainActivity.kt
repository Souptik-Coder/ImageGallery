package com.example.imagegallery.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagegallery.viewmodels.MainViewModel
import com.example.imagegallery.R
import com.example.imagegallery.adapters.PhotosAdapter
import com.example.imagegallery.databinding.ActivityMainBinding
import com.example.imagegallery.utils.NetworkResults
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val photosAdapter by lazy { PhotosAdapter() }
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
            layoutManager = GridLayoutManager(context, 2)
            adapter = photosAdapter
        }
        binding.retryBtn.setOnClickListener {
            hideError()
            viewModel.retryLoadPhotos()
        }
        viewModel.photoResponse.observe(this) { res ->
            when (res) {
                is NetworkResults.Error -> {
                    showError()
                    binding.progressBar.visibility = View.INVISIBLE
                }
                is NetworkResults.Loading -> binding.progressBar.visibility = View.VISIBLE
                is NetworkResults.Success -> {
                    photosAdapter.setData(res.data!!)
                    binding.progressBar.visibility = View.INVISIBLE
                    hideError()
                }
            }
        }
    }

    override fun onOptionsItemSelected(@NonNull item: MenuItem): Boolean {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private fun hideError() {
        binding.retryBtn.visibility = View.INVISIBLE
        binding.errorImageView.visibility = View.INVISIBLE
        binding.errorTextView.visibility = View.INVISIBLE
    }

    private fun showError(@StringRes resId: Int = R.string.unknown_error) {
        binding.retryBtn.visibility = View.VISIBLE
        binding.errorImageView.visibility = View.VISIBLE
        binding.errorTextView.visibility = View.VISIBLE
        binding.errorTextView.setText(resId)
    }
}