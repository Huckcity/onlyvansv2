package com.adamgibbons.onlyvansv2.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.adamgibbons.onlyvansv2.R
import com.adamgibbons.onlyvansv2.databinding.LoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import androidx.lifecycle.Observer
import com.adamgibbons.onlyvansv2.ui.home.Home

class Login : AppCompatActivity() {

    private lateinit var loginRegisterViewModel : LoginViewModel
    private lateinit var loginBinding : LoginBinding
    private lateinit var startForResult : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = LoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginBinding.emailSignInButton.setOnClickListener {
            signIn(loginBinding.fieldEmail.text.toString(),
                loginBinding.fieldPassword.text.toString())
        }
        loginBinding.emailCreateAccountButton.setOnClickListener {
            createAccount(loginBinding.fieldEmail.text.toString(),
                loginBinding.fieldPassword.text.toString())
        }
        loginBinding.googleSignInButton.setOnClickListener { googleSignIn() }
        loginBinding.googleSignInButton.setSize(SignInButton.SIZE_WIDE)
        loginBinding.googleSignInButton.setColorScheme(0)

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        loginRegisterViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginRegisterViewModel.liveFirebaseUser.observe(this)
        { firebaseUser ->
            if (firebaseUser != null)
                startActivity(Intent(this, Home::class.java))
        }

        loginRegisterViewModel.firebaseAuthManager.errorStatus.observe(this, Observer
        { status -> checkStatus(status) })

        setupGoogleSignInCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    private fun signIn(email: String, password: String) {
        Timber.d("signIn:$email")
        if (!validateForm()) { return }

        loginRegisterViewModel.login(email,password)
    }

    private fun createAccount(email: String, password: String) {
        Timber.d("createAccount:$email")
        if (!validateForm()) { return }

        loginRegisterViewModel.register(email,password)
    }

    private fun checkStatus(error:Boolean) {
        if (error)
            Toast.makeText(this,
                getString(R.string.auth_failed),
                Toast.LENGTH_LONG).show()
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = loginBinding.fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            loginBinding.fieldEmail.error = "Required."
            valid = false
        } else {
            loginBinding.fieldEmail.error = null
        }

        val password = loginBinding.fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            loginBinding.fieldPassword.error = "Required."
            valid = false
        } else {
            loginBinding.fieldPassword.error = null
        }
        return valid
    }

    private fun googleSignIn() {
        val signInIntent = loginRegisterViewModel.firebaseAuthManager
            .googleSignInClient.value!!.signInIntent

        startForResult.launch(signInIntent)
    }

    private fun setupGoogleSignInCallback() {
        startForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            val account = task.getResult(ApiException::class.java)
                            loginRegisterViewModel.authWithGoogle(account!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed
                            Timber.i( "Google sign in failed $e")
                            Snackbar.make(loginBinding.loginLayout, "Authentication Failed.",
                                Snackbar.LENGTH_SHORT).show()
                        }
                        Timber.i("DonationX Google Result $result.data")
                    }
                    RESULT_CANCELED -> {

                    } else -> { }
                }
            }
    }
}