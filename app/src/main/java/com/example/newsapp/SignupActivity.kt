package com.example.newsapp

import android.animation.ObjectAnimator
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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
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

    private fun validationSignUp() {
        val emailPattern = "^[A-Za-z0-9._%+-]+@(gmail|hotmail)\\.com$".toRegex()
        val passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#\$%^&+=!]).{8,}$".toRegex()
        val email = binding.emailEd.text.toString()
        val password = binding.passwordEd.text.toString()
        val cpassword = binding.cpasswordEd.text.toString()
        val name = binding.nameEd.text.toString()
        val country = binding.nationalty.text.toString().uppercase()
        if (name.isEmpty()) {
            binding.nameEd.error = "Please enter your name"
            shakeView(binding.nameEd)
        } else if (email.isEmpty()) {
            binding.emailEd.error = "Please enter your email"
            shakeView(binding.emailEd)
        } else if (!email.matches(emailPattern)) {
            binding.emailEd.error = "Only Gmail and Hotmail are allowed"


        } else if (password.isEmpty()) {
            binding.passwordEd.error = "Please enter your password"
            shakeView(binding.passwordEd)

        } else if (password.length <= 8) {
            binding.passwordEd.error = "Password should be at least 8 characters"
            shakeView(binding.passwordEd)
        } else if (!password.matches(passwordPattern)) {
            binding.passwordEd.error =
                "Password should contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
            shakeView(binding.passwordEd)
        } else if (cpassword.isEmpty()) {
            binding.cpasswordEd.error = "Please enter your confirm password"
            shakeView(binding.cpasswordEd)

        } else if (password != cpassword) {
            binding.passwordEd.error = "Password and confirm password should be same"
            binding.cpasswordEd.error = "Password and confirm password should be same"
            shakeView(binding.passwordEd)
            shakeView(binding.cpasswordEd)
        } else if (country.isEmpty()) {
            binding.nationalty.error = "Please enter your county"
            shakeView(binding.nationalty)

        } else {

            addUser(email, password, name, country)
        }
    }

    private fun addUser(email: String, password: String, name: String, country: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    verifyEmail()
                    dbAdd(email, name, country)

                } else {
                    Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show()
                }

            }
    }

    private fun verifyEmail() {
        var user = auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Verification link has been sent to your email",
                        Toast.LENGTH_SHORT
                    ).show()

                    startActivity(Intent(this, LoginActivity::class.java))

                }
            }
    }
//private fun checkUserExists(email: String, password: String, name: String, country: String) {
//
//    db.collection("users")
//        .whereEqualTo("email", email)
//        .get()
//        .addOnSuccessListener { documents ->
//            if (documents.isEmpty) {
//                Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show()
//            }else addUser(email, name, country,password)
//        }
//
//        .addOnFailureListener {
//            Toast.makeText(this, "Error checking user!", Toast.LENGTH_SHORT).show()
//        }
//}

    private fun dbAdd(email: String, name: String, country: String) {
        val u = user(name, email, country)
        db.collection("users")
            .add(u).addOnSuccessListener {
                it.update("id", it.id)
            }
    }

    fun shakeView(view: View) {
        val animator =
            ObjectAnimator.ofFloat(view, "translationX", 0f, 10f, -10f, 10f, -10f, 5f, -5f, 0f)
        animator.duration = 500
        animator.start()
    }
}