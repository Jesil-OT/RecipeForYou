package com.jesil.example.custom.recipeforyou.ui.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.jesil.example.custom.recipeforyou.R
import com.jesil.example.custom.recipeforyou.databinding.ItemRecipeBinding
import com.jesil.example.custom.recipeforyou.ui.model.RecipeDto
import com.jesil.example.custom.recipeforyou.ui.recipe.RecipeListFragment
import com.jesil.example.custom.recipeforyou.ui.utils.OnItemClickListener

class RecipePagingAdapter(private val _listener: OnItemClickListener) :
    PagingDataAdapter<RecipeDto, RecipePagingAdapter.RecipeListViewHolder>(UserComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeListViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null){
            holder.bind(currentItem)
        }
    }

    inner class RecipeListViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    if (item != null){
                        _listener.onItemClick(item)
                    }
                }
            }
        }
        private val requestOptions = RequestOptions()
            .placeholder(R.drawable.empty_plate)
            .error(R.drawable.ic_error)

            fun bind(recipe: RecipeDto) {
                binding.apply {
                    Glide.with(itemView)
                        .setDefaultRequestOptions(requestOptions)
                        .load(recipe.featuredImage)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(recipeListImage)
                    recipeListTitle.text = recipe.title
                    recipeListRating.text = recipe.rating.toString()
                }
            }
        }


    class UserComparator : DiffUtil.ItemCallback<RecipeDto>(){
        override fun areItemsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
            return oldItem.primaryKey == newItem.primaryKey
        }

        override fun areContentsTheSame(oldItem: RecipeDto, newItem: RecipeDto): Boolean {
            return oldItem == newItem
        }
    }
}