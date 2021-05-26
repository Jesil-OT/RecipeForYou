package com.jesil.example.custom.recipeforyou.ui.recipe

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.jesil.example.custom.recipeforyou.ui.api.RecipeApiService
import com.jesil.example.custom.recipeforyou.ui.model.RecipeResponse
import com.jesil.example.custom.recipeforyou.ui.repository.RecipeRepository
import com.jesil.example.custom.recipeforyou.ui.utils.Resources
import kotlinx.coroutines.launch

class RecipeListViewModel @ViewModelInject constructor(
   private val recipeRepository: RecipeRepository
): ViewModel() {

    private val currencyQuery = MutableLiveData("beef")

    val recipe = currencyQuery.switchMap { queryString->
        recipeRepository.getSearchResult(query = queryString).cachedIn(viewModelScope)

    }

    fun searchRecipes(query : String){
        currencyQuery.value = query
    }
}