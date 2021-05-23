package com.jesil.example.custom.recipeforyou.ui.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.jesil.example.custom.recipeforyou.ui.model.UserModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthRepository @Inject constructor(
    private val app : Application
) {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val rootRef = FirebaseFirestore.getInstance()
    private val usersRef = rootRef.collection("Users")
    val progressBarAuthAndGoogleButtonObserver = MutableLiveData<Boolean>()

    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential?, error : (e:Task<AuthResult>) -> Unit): MutableLiveData<UserModel> {
        progressBarAuthAndGoogleButtonObserver.value = true
        val authenticatedUserMutableLiveData = MutableLiveData<UserModel>()
        firebaseAuth.signInWithCredential(googleAuthCredential!!)
            .addOnCompleteListener { authTask: Task<AuthResult> ->
                if (authTask.isSuccessful) {
                    progressBarAuthAndGoogleButtonObserver.value = false
                    val isNewUser =
                        authTask.result!!.additionalUserInfo!!.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    if (firebaseUser != null) {
                        val uid = firebaseUser.uid
                        val name = firebaseUser.displayName
                        val email = firebaseUser.email
                        val imageUrl = firebaseUser.photoUrl!!
                        val user =
                            UserModel(
                                uid = uid,
                                name = name!!,
                                email = email!!,
                                imageUrl = imageUrl.path
                            )
                        user.isNew = isNewUser
                        authenticatedUserMutableLiveData.setValue(user)
                    }
                } else {
                    progressBarAuthAndGoogleButtonObserver.value = false
                    error(authTask)
                    Log.d("AuthRepository", "${authTask.exception!!.message}")
                }
            }
        return authenticatedUserMutableLiveData
    }

    fun createUserInFirestoreIfNotExists(authenticatedUser: UserModel): MutableLiveData<UserModel> {
        progressBarAuthAndGoogleButtonObserver.value = true
        val newUserMutableLiveData = MutableLiveData<UserModel>()
        val uidRef: DocumentReference = usersRef.document(authenticatedUser.uid)
        uidRef.get().addOnCompleteListener { uidTask: Task<DocumentSnapshot?> ->
            if (uidTask.isSuccessful) {
                progressBarAuthAndGoogleButtonObserver.value = false
                val document = uidTask.result
                if (!document!!.exists()) {
                    progressBarAuthAndGoogleButtonObserver.value = false
                    uidRef.set(authenticatedUser)
                        .addOnCompleteListener { userCreationTask: Task<Void?> ->
                            if (userCreationTask.isSuccessful) {
                                authenticatedUser.isCreated = true
                                newUserMutableLiveData.setValue(authenticatedUser)
                            } else {
                                Log.d("AuthRepository", "${userCreationTask.exception!!.message}")
                            }
                        }
                } else {
                    progressBarAuthAndGoogleButtonObserver.value = false
                    newUserMutableLiveData.setValue(authenticatedUser)
                }
            } else {
                progressBarAuthAndGoogleButtonObserver.value = false
                Log.d("AuthRepository", "${uidTask.exception!!.message}")
            }
        }
        return newUserMutableLiveData
    }
}