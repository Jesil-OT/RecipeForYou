package com.jesil.example.custom.recipeforyou.ui.recipe.recipeDetails

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.jesil.example.custom.recipeforyou.R
import com.jesil.example.custom.recipeforyou.databinding.RecipeListDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [RecipeListDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class RecipeListDetailsFragment : Fragment(R.layout.recipe_list_details_fragment) {

    private val args by navArgs<RecipeListDetailsFragmentArgs>()
    private var _binding : RecipeListDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val requestOptions = RequestOptions()
            .placeholder(R.drawable.empty_plate)
            .error(R.drawable.ic_broken_image)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = RecipeListDetailsFragmentBinding.bind(view)
        binding.apply {
            val recipe = args.recipe
            var ingredients = " "

            Glide.with(this@RecipeListDetailsFragment)
                    .setDefaultRequestOptions(requestOptions)
                    .load(recipe.featuredImage)
                    .into(recipeDetailsImageView)

            recipeDetailsPublisherCreated.text = "Created by ${recipe.publisher} in ${recipe.dateAdded}"
            recipeDetailsTitle.text = recipe.title
            recipeDetailsRating.text = recipe.rating.toString()

            for (ingredient in recipe.ingredients){
                ingredients += "$ingredient \n \n"
            }
            recipeDetailsIngredients.text = ingredients

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}