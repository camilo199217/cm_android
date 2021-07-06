package com.example.comunidadmascotas.ui

import android.accounts.AccountManagerFuture
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.comunidadmascotas.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 9001
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    override fun onStart() {
        super.onStart()
        val mGmailSignIn = findViewById<SignInButton>(R.id.sign_in_button)
        val account = GoogleSignIn.getLastSignedInAccount(this)
        Log.w("Sign In: ", account.toString())
        mGmailSignIn.setOnClickListener {
            signIn()
        }
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        } else {
            Log.w("result", result.toString())
            Toast.makeText(this, "No se pudo obtener los datos de la cuenta", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>?) {
        Log.w("Sign In: ", task.toString())
        try {
            val account = task?.getResult(ApiException::class.java)
            Log.w("account!!: ", task.toString())
            val idToken = account?.idToken
            Log.w("token!!: ", idToken.toString())
        } catch (e: ApiException) {
            Log.w("Sign In: ", "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient!!.signInIntent
        resultLauncher.launch(signInIntent)
    }
}