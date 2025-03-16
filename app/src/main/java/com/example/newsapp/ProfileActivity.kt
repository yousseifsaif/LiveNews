package com.example.newsapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            Firebase.firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userName = document.getString("name") ?: "No Name"
                        binding.userName.text = "Hi, $userName".toString()
                        Log.d("FirestoreField", "User Name: $userName")
                    } else {
                        Log.d("FirestoreField", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreField", "Error getting document", exception)
                }
        }


    }

    private fun getUserData() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userName = document.getString("name") ?: "User"
                        val userEmail = document.getString("email") ?: "No Email"
                        val userNational = document.getString("national") ?: "Not Provided"

                        binding.etName.setText(userName).toString()
                        binding.etEmail.setText(userEmail).toString()
                        binding.etNational.setText(userNational).toString()
                        binding.userName.text = "Hi, $userName".toString()

                        Log.d("FirestoreData", "User data loaded successfully")
                    } else {
                        Log.d("FirestoreData", "No such document")
                        Toast.makeText(this, "No user data found!", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreData", "Error getting document", exception)
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.e("FirestoreData", "User is not logged in")
            Toast.makeText(this, "User is not logged in!", Toast.LENGTH_SHORT).show()
        }
    }

}
