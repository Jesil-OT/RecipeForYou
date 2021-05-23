package com.jesil.example.custom.recipeforyou.ui.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

object HelperClass{
    fun showSnackBarMessage(binding: View, message: String){
        Snackbar.make(
                binding,
                message,
                Snackbar.LENGTH_LONG
        ).show()
    }
}