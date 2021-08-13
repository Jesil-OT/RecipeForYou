package com.jesil.example.custom.recipeforyou.ui.model

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
        @SerializedName("count")
        val count : Int,

        @SerializedName("next")
        val next : String,

        @SerializedName("previous")
        val previous : String?,

        @SerializedName("results")
        val results : List<RecipeDto>
)