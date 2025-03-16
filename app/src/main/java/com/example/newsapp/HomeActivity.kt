package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.api.Articles
import com.example.newsapp.api.NewsAdapter
import com.example.newsapp.api.NewsApi
import com.example.newsapp.api.NewsCallable
import com.example.newsapp.category.CategoryAdapter
import com.example.newsapp.category.CategoryList
import com.example.newsapp.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                R.id.action_logout -> {
                    showLogoutDialog()
                    true
                }

                else -> false
            }
        }
appearance()
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewH)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val categoryList = listOf(
            CategoryList(R.drawable.sports, "sports"),
            CategoryList(R.drawable.health, "health"),
            CategoryList(R.drawable.business, "business"),
            CategoryList(R.drawable.technology, "technology"),
            CategoryList(R.drawable.science, "science"),
            CategoryList(R.drawable.entertainment, "entertainment"),


            )
        val adapter = CategoryAdapter(categoryList)
        recyclerView.adapter = adapter
        binding.recyclerViewV.layoutManager = LinearLayoutManager(this)
        LoadNews()
    }



    private fun LoadNews() {
        val rertofit=Retrofit.Builder()
            .baseUrl("https://gnews.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api=rertofit.create(NewsCallable::class.java)
        api.GetNews().enqueue(object : Callback<NewsApi>{
            override fun onResponse(p0: Call<NewsApi>, p1: retrofit2.Response<NewsApi>) {
                val news=p1.body()
                val articles=news?.articles
                Log.d("articles",articles.toString())
                if (articles != null) {
                    getNews(articles)
                }

            }

            override fun onFailure(p0: Call<NewsApi>, p1: Throwable) {
                Log.d("articles","Error: ${p1.message}")

            }

        })

    }
    private fun appearance(){
        binding.recyclerViewV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    // إذا كان المستخدم يمرر لأسفل -> إخفاء القائمة الأفقية
                    binding.recyclerViewH.animate().alpha(0f).setDuration(100).start()
                } else if (dy < 0) {
                    // إذا كان المستخدم يمرر لأعلى -> إظهار القائمة الأفقية
                    binding.recyclerViewH.animate().alpha(1f).setDuration(100).start()
                }
            }
        })

    }
private fun getNews(articles: List<Articles>){
val adapter= NewsAdapter(this,articles)
    binding.recyclerViewV.adapter=adapter
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
}
