package com.jesil.example.custom.recipeforyou.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.jesil.example.custom.recipeforyou.ui.model.UserModel
import com.jesil.example.custom.recipeforyou.ui.repository.AuthRepository


class GoogleLoginViewModel @ViewModelInject constructor(
    private val authRepository: AuthRepository
)  : ViewModel() {

    val progress: MutableLiveData<Boolean> = authRepository.progressBarAuthAndGoogleButtonObserver

    private var _authenticatedUserLiveData = MutableLiveData<UserModel>()
    val authenticatedUserLiveData : LiveData<UserModel> get() = _authenticatedUserLiveData

    private var _createdUserLiveData = MutableLiveData<UserModel>()
    val createdUserLiveData : LiveData<UserModel> get() = _createdUserLiveData

    fun signInWithGoogle(googleAuthCredential: AuthCredential, error : (e: Task<AuthResult>) -> Unit) {
        _authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential = googleAuthCredential, error = error)
    }

    fun createUser(authenticatedUser: UserModel) {
        _createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser = authenticatedUser)
    }

}