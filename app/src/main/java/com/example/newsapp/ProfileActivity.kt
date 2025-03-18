package com.example.newsapp

import android.content.Context
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

        // جعل الحقول غير قابلة للتحرير
        binding.etName.isEnabled = false
        binding.etEmail.isEnabled = false
        binding.etNational.isEnabled = false

        // محاولة جلب البيانات من Firebase
        getUserData()
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

                        binding.etName.setText(userName)
                        binding.etEmail.setText(userEmail)
                        binding.etNational.setText(userNational)
                        binding.userName.text = "Hi, $userName"

                        // حفظ البيانات محليًا للاستخدام في وضع عدم الاتصال
                        saveUserDataLocally(userName, userEmail, userNational)

                        Log.d("FirestoreData", "User data loaded successfully")
                    } else {
                        Log.d("FirestoreData", "No such document")
                        Toast.makeText(this, "No user data found!", Toast.LENGTH_SHORT).show()
                        loadUserDataFromLocal() // جلب البيانات من SharedPreferences
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreData", "Error getting document", exception)
                    Toast.makeText(this, "Error: ${exception.message}", Toast.LENGTH_SHORT).show()
                    loadUserDataFromLocal() // جلب البيانات في حال الفشل
                }
        } else {
            Log.e("FirestoreData", "User is not logged in")
            Toast.makeText(this, "User is not logged in!", Toast.LENGTH_SHORT).show()
            loadUserDataFromLocal() // جلب البيانات من SharedPreferences
        }
    }

    // حفظ البيانات محليًا
    private fun saveUserDataLocally(name: String, email: String, national: String) {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString("name", name)
            .putString("email", email)
            .putString("national", national)
            .apply()
    }

    // جلب البيانات من SharedPreferences عند عدم توفر الإنترنت
    private fun loadUserDataFromLocal() {
        val sharedPreferences = getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val name = sharedPreferences.getString("name", "User") ?: "User"
        val email = sharedPreferences.getString("email", "No Email") ?: "No Email"
        val national = sharedPreferences.getString("national", "Not Provided") ?: "Not Provided"

        binding.etName.setText(name)
        binding.etEmail.setText(email)
        binding.etNational.setText(national)
        binding.userName.text = "Hi, $name"

        Toast.makeText(this, "Loaded offline data", Toast.LENGTH_SHORT).show()
    }
}
