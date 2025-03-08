package com.example.newsapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.newsapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding= ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.toolbar.inflateMenu(R.menu.three_dots)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.action_settings->true
                R.id.action_profile->true
                R.id.action_logout->{}
                else->false
            }
        }

    }
}