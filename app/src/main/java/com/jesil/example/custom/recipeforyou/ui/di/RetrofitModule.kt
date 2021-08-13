package com.jesil.example.custom.recipeforyou.ui.di

import com.jesil.example.custom.recipeforyou.ui.api.RecipeApiService
import com.jesil.example.custom.recipeforyou.ui.constants.Constants.RECIPE_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit =
        Retrofit.Builder()
            .baseUrl(RECIPE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : RecipeApiService =
        retrofit.create(RecipeApiService::class.java)

}