package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newsapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        binding.signupBtn.setOnClickListener {
            validationSignUp()
        }
    }

  private  fun validationSignUp() {
        val email = binding.emailEd.text.toString()
        val password = binding.passwordEd.text.toString()
        val cpassword = binding.cpasswordEd.text.toString()
        if (email.isEmpty() || password.isEmpty()||cpassword.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }  else if (password.length <= 8) {
            Toast.makeText(this, "Password should be at least 8 characters", Toast.LENGTH_SHORT)
                .show()}

        else if (password!=cpassword) {
            Toast.makeText(this, "Password and confirm password should be same", Toast.LENGTH_SHORT)
                .show()
        }
         else {
             addUser(email,password)

        }
    }

    private fun addUser(email:String,password:String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener (this){task->
                if (task.isSuccessful) {
                    verifyEmail()
                }
                else{Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show()}

            }
    }

    private fun verifyEmail() {
        var user = auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification link has been sent to your email", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))

                }
            }
    }
}