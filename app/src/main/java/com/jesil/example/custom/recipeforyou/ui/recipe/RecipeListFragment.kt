package com.jesil.example.custom.recipeforyou.ui.recipe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.jesil.example.custom.recipeforyou.R
import com.jesil.example.custom.recipeforyou.databinding.RecipeListFragmentBinding
import com.jesil.example.custom.recipeforyou.ui.model.RecipeDto
import com.jesil.example.custom.recipeforyou.ui.recipe.recipeDetails.RecipeListDetailsFragment
import com.jesil.example.custom.recipeforyou.ui.utils.OnItemClickListener
import com.jesil.example.custom.recipeforyou.ui.utils.adapter.RecipePagingAdapter
import com.jesil.example.custom.recipeforyou.ui.utils.adapter.RecipePhotoLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [RecipeListDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

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
                adapter = recipeListAdapter.withLoadStateHeaderAndFooter(
                        header = RecipePhotoLoadStateAdapter{
                            recipeListAdapter.retry()
                        },
                        footer = RecipePhotoLoadStateAdapter{
                            recipeListAdapter.retry()
                        }
                )
            }
            recipeButtonRetry.setOnClickListener {
                recipeListAdapter.retry()
            }
        }
        viewModel.recipe.observe(viewLifecycleOwner){ result ->
            recipeListAdapter.submitData(viewLifecycleOwner.lifecycle,result)
            Log.d("TAG", "show paging data $result.")
        }

        recipeListAdapter.addLoadStateListener { loadState ->

            binding.apply {
                recipeRecyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                recipeButtonRetry.isVisible = loadState.source.refresh is LoadState.Error
                recipeTextViewError.isVisible = loadState.source.refresh is LoadState.Error
                recipeNoConnectionImage.isVisible = loadState.source.refresh is LoadState.Error

                // shimmer
                if (loadState.source.refresh is LoadState.Loading){
                    shimmerViewContainer.apply{
                        isVisible = loadState.source.refresh is LoadState.Loading
                        startShimmer()
                    }
                    recipeProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                }
                else {
                    shimmerViewContainer.apply{
                        visibility = View.GONE
                        stopShimmer()
                    }
                    recipeProgressBar.visibility = View.GONE
                }

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

    override fun onResume() {
        super.onResume()
        binding.shimmerViewContainer.startShimmer()
    }

    override fun onPause() {
        binding.shimmerViewContainer.stopShimmer()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(recipe: RecipeDto) {
        val action = RecipeListFragmentDirections.actionRecipeListFragmentToRecipeListDetailsFragment(recipe = recipe)
        findNavController().navigate(action)
    }

}