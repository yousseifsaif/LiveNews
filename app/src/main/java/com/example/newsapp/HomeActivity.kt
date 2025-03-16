package com.example.newsapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.Response
import com.example.newsapp.category.CategoryAdapter
import com.example.newsapp.category.CategoryList
import com.example.newsapp.databinding.ActivityHomeBinding
import retrofit2.Call
import retrofit2.Callback

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

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val categoryList=listOf(
            CategoryList(R.drawable.sports,"sports"),
            CategoryList(R.drawable.health,"health"),
            CategoryList(R.drawable.business,"business"),
            CategoryList(R.drawable.technology,"technology"),
            CategoryList(R.drawable.science,"science"),
            CategoryList(R.drawable.entertainment,"entertainment"),


            )
        val adapter = CategoryAdapter(categoryList)
        recyclerView.adapter = adapter



//        getSources()
    }

//    private fun getSources() {
//        binding.tab.
//        ApiManager.getApis().getNewsSources("5ff816d5e910a9bda80be039e253304c", "eg", "general")
//            .enqueue(object :Callback<Response>{
//                override fun onResponse(p0: Call<Response>, p1: retrofit2.Response<Response>) {
//                    if(p1.isSuccessful){
//                    }else{
//                        Toast.makeText(this@HomeActivity,"Error Occurred", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(p0: Call<Response>, p1: Throwable) {
//                }
//            })
//    }

    private fun showResourcesTabLayout() {

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
