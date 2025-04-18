package com.example.newsapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newsapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.loginbtn.setOnClickListener {
            validationLogin()
        }
        binding.register.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))

        }
        binding.forgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
    }


}
    fun validationLogin() {
        val email = binding.emailEd.text.toString()
        val password = binding.passwordEd.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            binding.emailEd.error = "Please enter your mail"
            binding.passwordEd.error = "please enter your password"
            shakeView(binding.emailEd)
            shakeView(binding.passwordEd)
//            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        } else {
            login(email, password)
        }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                if (auth.currentUser!!.isEmailVerified == true) {

                    sharedPref()
                    binding.loading.visibility = View.VISIBLE
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, HomeActivity::class.java)
                        )
                        finish()
                    }, 3000)



                } else {
                    binding.loading.visibility = View.INVISIBLE

                    Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show()
                }
            } else Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharedPref() {
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.apply()
    }

    fun shakeView(view: View) {
        val animator = ObjectAnimator.ofFloat(view, "translationX", 0f, 10f, -10f, 10f, -10f, 5f, -5f, 0f)
        animator.duration = 500
        animator.start()
    }
}
