package com.jesil.example.custom.recipeforyou.ui.recipe

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jesil.example.custom.recipeforyou.ui.api.RecipeApiService
import com.jesil.example.custom.recipeforyou.ui.model.RecipeResponse
import com.jesil.example.custom.recipeforyou.ui.utils.Resources
import kotlinx.coroutines.launch

class RecipeListViewModel @ViewModelInject constructor(
    recipeApi : RecipeApiService
): ViewModel() {

    private val _recipeLiveData = MutableLiveData<RecipeResponse>()
    val recipeLiveData : LiveData<RecipeResponse> get() = _recipeLiveData


    init {
        viewModelScope.launch {
            val recipes = recipeApi.getRecipes(
                authorization = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48",
                page = 1,
                query = "beef" )
            _recipeLiveData.value = recipes
        }
    }
}