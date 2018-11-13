package com.danielwalkerapp.kotlinmessenger

import android.content.Intent
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
                        val intent = Intent(this, LatestMessagesActivity::class.java)
                        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        Log.d("Login", "Login failed ${it.message}")
                    }

        }
    }
}