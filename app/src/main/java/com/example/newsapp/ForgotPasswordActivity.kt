package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newsapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        enableEdgeToEdge()
        binding.forgotPasswordbtn.setOnClickListener {
            val email = binding.emailEd.text.trim().toString()
            if (email.isEmpty()) {
                binding.emailEd.error = "Please enter your email"
            } else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        binding.loading.visibility = View.VISIBLE
                        Toast.makeText(this, "Link sent to your email", Toast.LENGTH_SHORT).show()
                        binding.loading.visibility = View.INVISIBLE
                    } else {
                        Toast.makeText(
                            this,
                            "Failed to send email: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.loading.visibility = View.INVISIBLE


                    }
                }
            }


        }
    }
}