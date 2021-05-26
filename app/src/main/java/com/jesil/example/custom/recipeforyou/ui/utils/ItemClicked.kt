package com.jesil.example.custom.recipeforyou.ui.utils

import com.jesil.example.custom.recipeforyou.ui.model.RecipeDto


interface OnItemClickListener{
    fun onItemClick(recipe: RecipeDto)
}