package com.jesil.example.custom.recipeforyou.ui.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jesil.example.custom.recipeforyou.databinding.RecipeLoadStateFooterBinding


class RecipePhotoLoadStateAdapter(private val retry: () -> Unit)
    : LoadStateAdapter<RecipePhotoLoadStateAdapter.RecipePhotoLoadStateViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecipePhotoLoadStateViewHolder {
        return RecipePhotoLoadStateViewHolder(
            RecipeLoadStateFooterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecipePhotoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState = loadState)
    }

   inner class RecipePhotoLoadStateViewHolder(
        private val binding: RecipeLoadStateFooterBinding
        ) : RecyclerView.ViewHolder(binding.root){

            init {
                binding.recipeFooterButtonRetry.setOnClickListener {
                    retry.invoke()
                }
            }

       fun bind(loadState : LoadState){
           binding.apply {
               recipeFooterProgressBar.isVisible = loadState is LoadState.Loading
               recipeFooterNetworkError.isVisible = loadState !is LoadState.Loading
               recipeFooterInternetErrorTalk.isVisible = loadState !is LoadState.Loading
               recipeFooterWifiOffImage.isVisible = loadState !is LoadState.Loading
               recipeFooterButtonRetry.isVisible = loadState !is LoadState.Loading
           }
       }
    }

}