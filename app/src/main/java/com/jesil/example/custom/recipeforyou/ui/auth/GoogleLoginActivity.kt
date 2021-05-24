package com.jesil.example.custom.recipeforyou.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.jesil.example.custom.recipeforyou.R
import com.jesil.example.custom.recipeforyou.databinding.ActivityGoogleLoginBinding
import com.jesil.example.custom.recipeforyou.ui.RecipeActivity
import com.jesil.example.custom.recipeforyou.ui.constants.Constants.RC_SIGN_IN
import com.jesil.example.custom.recipeforyou.ui.constants.Constants.USER
import com.jesil.example.custom.recipeforyou.ui.model.UserModel
import com.jesil.example.custom.recipeforyou.ui.utils.HelperClass.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GoogleLoginActivity : AppCompatActivity() {

    private val viewModel : GoogleLoginViewModel by viewModels()
    private lateinit var binding : ActivityGoogleLoginBinding
    private lateinit var googleSignInClient : GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initGoogleSignInClient()
        binding = ActivityGoogleLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            googleSignInButton.setOnClickListener { v ->
                signInWithGoogle()
            }
            viewModel.progress.observe(this@GoogleLoginActivity){ state ->
                if (state == true){
                    progressBarGoogleLogin.visibility = View.VISIBLE
                    googleSignInButton.isEnabled = state.or(false)
                }
                else{
                    progressBarGoogleLogin.visibility = View.GONE
                    googleSignInButton.isEnabled = state.or(true)
                }
            }
        }
    }

    private fun initGoogleSignInClient(){
        val googleSignInOptions = GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun signInWithGoogle() {
        viewModel.progress.value = true
        val signInIntent : Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        viewModel.signInWithGoogle(googleAuthCredential) { e: Task<AuthResult> ->
            e.exception!!.message?.let { showSnackBarMessage(binding = binding.root, it) }
        }
        viewModel.authenticatedUserLiveData.observe(this) { authenticatedUser ->
            if (authenticatedUser.isNew) {
                createNewUser(authenticatedUser)
            } else {
                showSnackBarMessage(
                    binding = binding.root,
                    message = "Sign In as ${authenticatedUser.email}"
                )
                goToRecipeActivity(authenticatedUser)
            }
        }
    }

    private fun createNewUser(authenticatedUser: UserModel) {
        viewModel.createUser(authenticatedUser)
        viewModel.createdUserLiveData.observe(this) { user ->
            if (user.isCreated) {
                showSnackBarMessage(
                    binding = binding.root,
                    message = "Welcome, ${user.name}"
                )
            }
            showSnackBarMessage(
                binding = binding.root,
                message = "Sign in with ${user.email}"
            )
            goToRecipeActivity(user)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account != null){
                    getGoogleAuthCredential(
                        googleSignInAccount = account
                    )
                }
            } catch (e: ApiException) {
                viewModel.progress.value = false
                // Google Sign In failed, update UI appropriately
                Log.w("GoogleLoginFragment", e.localizedMessage!!)
                showSnackBarMessage(
                    binding = binding.root,
                    message = "Google Sign In failed ${e.message}"
                )
            }
        }
    }

    private fun goToRecipeActivity(user: UserModel) {
        val intent = Intent(this@GoogleLoginActivity, RecipeActivity::class.java)
        intent.putExtra(USER, user)
        startActivity(intent)
        finish()
    }

}