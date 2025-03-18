package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.api.Articles
import com.example.newsapp.api.NewsAdapter
import com.example.newsapp.databinding.ActivityFavoritesBinding
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private val favoriteArticles = mutableListOf<Articles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFavorites()
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.action_logout -> {
                    showLogoutDialog()
                    true
                }
                else -> false
            }
        }
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFavorites.adapter = NewsAdapter(this, favoriteArticles) { article ->
            favoriteArticles.remove(article)
            saveFavorites()
            binding.recyclerViewFavorites.adapter?.notifyDataSetChanged()
        }
    }

    private fun loadFavorites() {
        val sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE)
        val json = sharedPreferences.getString("favorites", null)
        if (json != null) {
            val type = object : TypeToken<List<Articles>>() {}.type
            favoriteArticles.addAll(Gson().fromJson(json, type))
        }
    }

    private fun saveFavorites() {
        val sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(favoriteArticles)
        editor.putString("favorites", json)
        editor.apply()
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setMessage("Do you want to Log Out?")
            .setPositiveButton("Yes") { dialog, _ ->
                logout()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}