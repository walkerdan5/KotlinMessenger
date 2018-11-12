package com.danielwalkerapp.kotlinmessenger

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        performLogin()
    }

    private fun performLogin(){
        button_Login.setOnClickListener {
            val email = login_email_editText.text.toString()
            val password = login_password_editText.text.toString()
            Log.d("Login", "Attempted login with email/pass")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if(!it.isSuccessful) return@addOnCompleteListener

                        Log.d("Login", "Login successful with email/pass")
                    }
                    .addOnFailureListener {
                        Log.d("Login", "Login failed ${it.message}")
                    }

        }
    }
}