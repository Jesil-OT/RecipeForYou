package com.jesil.example.custom.recipeforyou.ui.recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import com.jesil.example.custom.recipeforyou.R
import com.jesil.example.custom.recipeforyou.databinding.RecipeListFragmentBinding
import com.jesil.example.custom.recipeforyou.ui.model.RecipeDto
import com.jesil.example.custom.recipeforyou.ui.utils.OnItemClickListener
import com.jesil.example.custom.recipeforyou.ui.utils.adapter.RecipePagingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment(R.layout.recipe_list_fragment), OnItemClickListener {

    private val viewModel: RecipeListViewModel by viewModels()
    private var _binding : RecipeListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = RecipeListFragmentBinding.bind(view)

        val recipeListAdapter = RecipePagingAdapter(this)
        binding.apply {
            recipeRecyclerView.apply {
                setHasFixedSize(true)
                adapter = recipeListAdapter
            }
            recipeButtonRetry.setOnClickListener {
                recipeListAdapter.retry()
            }
        }
        viewModel.recipe.observe(viewLifecycleOwner){
            recipeListAdapter.submitData(viewLifecycleOwner.lifecycle,it)
            Log.d("TAG", "show paging data $it.")
        }

        recipeListAdapter.addLoadStateListener { loadState ->

            binding.apply {
                recipeProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recipeRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                recipeButtonRetry.isVisible = loadState.source.refresh is LoadState.Error
                recipeTextViewError.isVisible = loadState.source.refresh is LoadState.Error


                // empty textView
                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        recipeListAdapter.itemCount < 1){
                    recipeRecyclerView.isVisible = false
                    recipeTextViewEmpty.isVisible  = true
                }else{
                    recipeTextViewEmpty.isVisible = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(recipe: RecipeDto) {
        TODO("Not yet implemented")
    }

}