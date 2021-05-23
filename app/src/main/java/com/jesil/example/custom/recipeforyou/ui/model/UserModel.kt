package com.jesil.example.custom.recipeforyou.ui.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(
        var uid: String,
        var name: String,
        var email: String,
        val imageUrl : String?,
        var isAuthenticated: Boolean = false,
        var isNew: Boolean = false,
        var isCreated: Boolean = false
) : Parcelable