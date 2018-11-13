package com.danielwalkerapp.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
            perfromRegistration()
        }

        selectphoto_button_register.setOnClickListener {
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

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("RegisterActivity", "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            photo_imageview_register.setImageBitmap(bitmap)
            selectphoto_button_register.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            selectphoto_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun perfromRegistration() {
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

                    uploadImageToFirebaseStorage()
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
                }
    }


    private fun uploadImageToFirebaseStorage() {
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Successfully uploaded image  {${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.d("RegisterActivity", "File Location:  $it")

                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to upload image to Firebase")

                }
    }

    private fun saveUserToDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?:""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(editnametext_register.text.toString(), profileImageUrl, uid)

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegisterActivity", "Saved user to Realtime Database")

                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    //flag will stop the user from returning to the register page after registering
                    intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }.addOnFailureListener {
                    Log.d("RegisterActivity", "Failed to add the user to the database")

                }

    }


}

class User(val username: String, val profileImageUrl: String, val uid: String)