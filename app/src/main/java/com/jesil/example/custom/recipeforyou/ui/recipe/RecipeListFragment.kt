package com.jesil.example.custom.recipeforyou.ui.recipe

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jesil.example.custom.recipeforyou.R
import com.jesil.example.custom.recipeforyou.databinding.RecipeListFragmentBinding
import com.jesil.example.custom.recipeforyou.ui.utils.adapter.RecipeListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment(R.layout.recipe_list_fragment) {

    private val viewModel: RecipeListViewModel by viewModels()
    private var _binding : RecipeListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = RecipeListFragmentBinding.bind(view)

        val recipeListAdapter = RecipeListAdapter()
        binding.apply {
            recipeRecyclerView.apply {
                adapter = recipeListAdapter
            }
        }
        viewModel.recipeLiveData.observe(viewLifecycleOwner){
            recipeListAdapter.submitList(it.results)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}