package com.danielwalkerapp.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class RegisterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        register_button_register.setOnClickListener {
            perfromRegistration()
        }

        register_photo_btn.setOnClickListener {
            Log.d("RegisterActivity", "Try to show photo selector ")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }


        haveAccount_text_register.setOnClickListener {
            Log.d("RegisterActivity", "Try to show login activity")

            //launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("MainActivity",  "Photo was selected")

            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            register_photo_btn.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun perfromRegistration(){
        val name = editnametext_register.text.toString()
        val email = editemailtext_register.text.toString()
        val password = editpasswordtext_register.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter an email and password", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("RegisterActivity", "name is: " + name)
        Log.d("RegisterActivity", "email is: " + email)
        Log.d("RegisterActivity", "password is:  $password")

        //FireBase Authentication using Kotlin implementation
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener

                    //else if the creation was successful
                    Log.d("RegisterActivity", "Successfully created user with UID: ${it.result!!.user.uid} ")
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }



