package com.adamgibbons.onlyvansv2.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.adamgibbons.onlyvansv2.firebase.FirebaseAuthManager
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser

class LoginViewModel (app: Application) : AndroidViewModel(app) {
    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser

    fun login(email: String?, password: String?) {
        firebaseAuthManager.login(email, password)
    }

    fun register(email: String?, password: String?) {
        firebaseAuthManager.register(email, password)
    }

    fun authWithGoogle(acct: GoogleSignInAccount) {
        firebaseAuthManager.firebaseAuthWithGoogle(acct)
    }
}