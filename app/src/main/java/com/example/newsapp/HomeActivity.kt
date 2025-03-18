package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.api.Articles
import com.example.newsapp.api.NewsAdapter
import com.example.newsapp.api.NewsApi
import com.example.newsapp.api.NewsCallable
import com.example.newsapp.category.CategoryAdapter
import com.example.newsapp.category.CategoryList
import com.example.newsapp.databinding.ActivityHomeBinding
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val favoriteArticles = mutableListOf<Articles>()
    private var selectedCategory: String? = null
    private lateinit var adapterr: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (isDarkModeEnabled(this)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val countryCode = sharedPreferences.getString("country_code", "eg") ?: "eg"
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

        loadFavorites()
        binding.fav.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewH)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val categoryList = listOf(
            CategoryList(R.drawable.general, "general"),
            CategoryList(R.drawable.sports, "sports"),
            CategoryList(R.drawable.health, "health"),
            CategoryList(R.drawable.business, "business"),
            CategoryList(R.drawable.technology, "technology"),
            CategoryList(R.drawable.science, "science"),
            CategoryList(R.drawable.movies, "entertainment")
        )
        selectedCategory = "general"
        LoadNews(selectedCategory!!, countryCode )

        adapterr = CategoryAdapter(categoryList, { category ->
            selectedCategory = category
            LoadNews(category, countryCode)
            adapterr.updateSelectedCategory(category)
        }, selectedCategory)

        recyclerView.adapter = adapterr
        binding.recyclerViewV.layoutManager = LinearLayoutManager(this)
    }

    private fun LoadNews(category: String, country: String) {
        binding.animationView.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://gnews.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NewsCallable::class.java)

        api.GetNews(category, country).enqueue(object : Callback<NewsApi> {  // ✅ استخدم المتغير مباشرة
            override fun onResponse(p0: Call<NewsApi>, p1: retrofit2.Response<NewsApi>) {
                val news = p1.body()
                val articles = news?.articles
                Log.d("articles", articles.toString())
                if (articles != null) {
                    getNews(articles)
                }
                binding.animationView.visibility = View.GONE
            }

            override fun onFailure(p0: Call<NewsApi>, p1: Throwable) {
                Log.d("articles", "Error: ${p1.message}")
                binding.animationView.visibility = View.GONE
                Toast.makeText(
                    this@HomeActivity,
                    "Check Internet Connection !!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun getNews(articles: List<Articles>) {
        val adapter = NewsAdapter(this, articles) { article ->
            if (!favoriteArticles.contains(article)) {
                favoriteArticles.add(article)
                saveFavorites()
                Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.recyclerViewV.adapter = adapter
    }

    private fun appearance() {
        binding.recyclerViewV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    binding.recyclerViewH.animate().alpha(0f).setDuration(100).start()
                } else if (dy < 0) {
                    binding.recyclerViewH.animate().alpha(1f).setDuration(100).start()
                }
            }
        })
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

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.clear()
        menuInflater.inflate(R.menu.tool_menu, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    private fun saveFavorites() {
        val sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(favoriteArticles)
        editor.putString("favorites", json)
        editor.apply()
    }

    private fun loadFavorites() {
        val sharedPreferences = getSharedPreferences("Favorites", MODE_PRIVATE)
        val json = sharedPreferences.getString("favorites", null)
        if (json != null) {
            val type = object : TypeToken<List<Articles>>() {}.type
            favoriteArticles.addAll(Gson().fromJson(json, type))
        }
    }
    fun setDarkModeEnabled(context: Context, isEnabled: Boolean) {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("dark_mode", isEnabled).apply()

        if (isEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    fun isDarkModeEnabled(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("dark_mode", false)
    }
}