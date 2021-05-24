package com.jesil.example.custom.recipeforyou.ui.favorites

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jesil.example.custom.recipeforyou.R
import com.jesil.example.custom.recipeforyou.databinding.FavoritesListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesListFragment : Fragment(R.layout.favorites_list_fragment) {

    private val viewModel: FavoritesListViewModel by viewModels()
    private var _binding : FavoritesListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FavoritesListFragmentBinding.bind(view)
        binding.apply {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }



}