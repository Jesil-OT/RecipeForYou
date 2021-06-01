package com.jesil.example.custom.recipeforyou.ui.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeDto(
    @SerializedName("pk")
    val primaryKey : Int,

    val title : String,

    val publisher : String,

    @SerializedName("featured_image")
    val featuredImage : String,

    val rating : Int,

    @SerializedName("source_url")
    val sourceUrl : String,

    val description : String,

    @SerializedName("cooking_instructions")
    val cookingInstructions : String? = null,

    val ingredients : ArrayList<String>,

    @SerializedName("date_added")
    val dateAdded : String,

    @SerializedName("date_updated")
    val dateUpdated : String,

    @SerializedName("long_date_added")
    val longDateAdded : Int,

    @SerializedName("long_date_updated")
    val longDateUpdated : Int

) : Parcelable