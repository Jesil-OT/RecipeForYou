package com.jesil.example.custom.recipeforyou.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.jesil.example.custom.recipeforyou.R
import com.jesil.example.custom.recipeforyou.databinding.ActivityRecipeBinding
import com.jesil.example.custom.recipeforyou.ui.auth.GoogleLoginActivity
import com.jesil.example.custom.recipeforyou.ui.constants.Constants.USER
import com.jesil.example.custom.recipeforyou.ui.model.UserModel
import com.jesil.example.custom.recipeforyou.ui.utils.HelperClass.showSnackBarMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.log

@AndroidEntryPoint
class RecipeActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener  {
    private lateinit var binding : ActivityRecipeBinding
    private lateinit var googleSignInClient : GoogleSignInClient
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var photoUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.mainBottomNavigation.setupWithNavController(navController)

        initGoogleSignInClient()
        setSupportActionBar(binding.recipeActivityToolbar.root)
        val user = getUserFromIntent()
        showSnackBarMessage(
                binding = binding.root,
                message = "logged in as ${user?.email}  ✓"
        )
        binding.apply {
            recipeActivityToolbar.root.apply {
                title = user?.name
                subtitle = user?.email
            }
//            Glide.with(this@RecipeActivity)
//                    .load(user?.imageUrl)
//                    .into(recipeActivityToolbar.userImageView)
//            Log.d("TAG", "onCreate: ${user?.imageUrl} ")
        }
    }

    private fun getUserFromIntent(): UserModel? {
        return intent.getParcelableExtra(USER) as UserModel?
    }

    private fun initGoogleSignInClient(){
        val googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        val user = getUserFromIntent()
       return when (item.itemId) {
            R.id.sign_out -> {
                showDialogMessage(context = this, user = user?.name)
                true
            }
            R.id.setting -> {
                // TODO : Navigate to Settings Activity
                true
            }
           R.id.profile ->{true}
           R.id.search ->{true}
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun showDialogMessage(context: RecipeActivity, user : String?) {
        val alertDialog = MaterialAlertDialogBuilder(context)

        alertDialog.apply {
            setTitle("Sign out")
            setMessage("Are you sure you want to Sign out $user")
            setPositiveButton(
                    "Yes"
            ) { dialog, which ->
                signOutUser()
                finish()
            }
            setNegativeButton(
                    "No"
            ) { dialog, which ->
            }
            show()
        }
    }

    private fun signOutUser() {
        signOutFirebase();
        signOutGoogle();
    }

    private fun signOutFirebase() {
        firebaseAuth.signOut()
    }

    private fun signOutGoogle() {
        googleSignInClient.signOut()
    }

    override fun onAuthStateChanged(p0: FirebaseAuth) {
        val currentUser = p0.currentUser
        if (currentUser == null){
            goToGoogleLoginActivity()
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener(this)
        if (firebaseAuth.currentUser != null){
            val currentUser = firebaseAuth.currentUser
            photoUri = currentUser?.photoUrl!!

        }
    }

    override fun onStop() {
        super.onStop()
        firebaseAuth.removeAuthStateListener(this)
    }

    private fun goToGoogleLoginActivity() {
        startActivity(Intent(this, GoogleLoginActivity::class.java))
        finish()
    }

}