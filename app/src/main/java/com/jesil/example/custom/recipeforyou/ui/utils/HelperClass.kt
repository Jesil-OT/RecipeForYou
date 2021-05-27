package com.jesil.example.custom.recipeforyou.ui.utils

import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.jesil.example.custom.recipeforyou.ui.RecipeActivity

object HelperClass{
    fun showSnackBarMessage(
            binding: View,
            message: String)
    {
        Snackbar.make(
                binding,
                message,
                Snackbar.LENGTH_LONG
        ).show()
    }

    fun showDialogMessage(
            context: RecipeActivity,
            user : String?,
            signOutUser : () -> Unit
    ) {
        val alertDialog = MaterialAlertDialogBuilder(context)

        alertDialog.apply {
            setTitle("Sign out")
            setMessage("Are you sure you want to Sign out $user")
            setPositiveButton(
                    "Yes"
            ) { dialog, which ->
                signOutUser()
            }
            setNegativeButton(
                    "No"
            ) { dialog, which ->
            }
            show()
        }
    }
}