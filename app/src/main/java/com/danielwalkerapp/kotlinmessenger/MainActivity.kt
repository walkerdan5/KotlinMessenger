package com.danielwalkerapp.kotlinmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            val name = editnametext_register.text.toString()
            val email = editemailtext_register.text.toString()
            val password = editpasswordtext_register.text.toString()

            Log.d("MainActivity", "name is: " + name)
            Log.d("MainActivity", "email is: " + email)
            Log.d("MainActivity", "password is:  $password")
        }

        haveAccount_text_register.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")

            //launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
