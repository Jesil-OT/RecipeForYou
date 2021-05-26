package com.jesil.example.custom.recipeforyou.ui.utils.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jesil.example.custom.recipeforyou.ui.api.RecipeApiService
import com.jesil.example.custom.recipeforyou.ui.model.RecipeDto
import retrofit2.HttpException
import java.io.IOException

class RecipePagingSource(
    private val recipeApiService: RecipeApiService,
    private val query: String
) : PagingSource<Int, RecipeDto>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RecipeDto> {

        return try {
            val position = params.key ?: 1

            val response = recipeApiService.getRecipes(
                authorization = "Token 9c8b06d329136da358c2d00e76946b0111ce2c48", // Token 9c8b06d329136da358c2d00e76946b0111ce2c48
                page = position,
                query = query
            )

            LoadResult.Page(
                data = response.results,
                prevKey = null,
                nextKey = if (response.results.isEmpty()) null else position + 1
            )
        }
        catch (exception : IOException){
            Log.d("TAG", "load: $exception")
            LoadResult.Error(
                exception
            )
        }
        catch (httpException : HttpException){
            Log.d("TAG", "load: ${httpException.message}")
            LoadResult.Error(
                httpException
            )
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RecipeDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}