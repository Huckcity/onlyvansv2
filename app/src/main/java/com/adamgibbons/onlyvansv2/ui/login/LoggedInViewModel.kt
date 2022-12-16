package com.adamgibbons.onlyvansv2.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.adamgibbons.onlyvansv2.firebase.FirebaseAuthManager
import com.google.firebase.auth.FirebaseUser

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser
    var loggedOut : MutableLiveData<Boolean> = firebaseAuthManager.loggedOut

    fun logOut() {
        firebaseAuthManager.logOut()
    }
}