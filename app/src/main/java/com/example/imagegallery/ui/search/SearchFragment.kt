package com.example.imagegallery.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagegallery.R
import com.example.imagegallery.adapters.PhotosAdapter
import com.example.imagegallery.databinding.FragmentSearchBinding
import com.example.imagegallery.utils.NetworkResults
import com.example.imagegallery.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val photosAdapter by lazy { PhotosAdapter() }

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)
        setUpRecyclerView()
        viewModel.searchPhotoResponse.observe(viewLifecycleOwner) { res ->
            binding.progressBar.isVisible = res is NetworkResults.Loading
            binding.recyclerView.isVisible = res !is NetworkResults.Loading
            binding.searchHintTextView.isVisible = res == null
            when (res) {
                is NetworkResults.Error -> {
                    showRetrySnackbar(res.messageResId!!)
                    viewModel.setErrorHandled()
                }
                is NetworkResults.Loading -> Unit
                is NetworkResults.Success -> photosAdapter.setData(res.data!!)
            }
        }
    }

    private fun showRetrySnackbar(@StringRes messageResId: Int) {
        Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_INDEFINITE).setAction(
            "Retry"
        ) {
            viewModel.retrySearchPhoto()
        }.show()
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = photosAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_fragment_options_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchItem.expandActionView()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank())
                    viewModel.searchPhotoDebounced(newText)
                return true
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}