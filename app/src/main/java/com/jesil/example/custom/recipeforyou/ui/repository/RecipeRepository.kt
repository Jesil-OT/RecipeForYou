package com.jesil.example.custom.recipeforyou.ui.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.jesil.example.custom.recipeforyou.ui.api.RecipeApiService
import com.jesil.example.custom.recipeforyou.ui.utils.data.RecipePagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipeRepository @Inject constructor(
    private val recipeApiService: RecipeApiService
){
    fun getSearchResult(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 15,
                maxSize = 300,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RecipePagingSource(
                    recipeApiService = recipeApiService, query = query)
            }
        ).liveData
}

// before pageSize = 30 and maxSize = 161