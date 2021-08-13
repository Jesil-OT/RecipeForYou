package com.jesil.example.custom.recipeforyou.ui.api

import com.jesil.example.custom.recipeforyou.ui.model.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeApiService {

   // https://food2fork.ca/api/recipe/search/?page=1&query=beef

    @GET("search")
    suspend fun getRecipes(
        @Header("Authorization" ) authorization: String,
        @Query("page") page: Int,
        @Query("query") query : String
    ) : RecipeResponse
}