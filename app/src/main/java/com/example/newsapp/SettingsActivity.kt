package com.example.newsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.newsapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val sharedPreferences by lazy { getSharedPreferences("AppSettings", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val countries = mapOf(
            "Select Country" to "",
            "Egypt" to "eg",
            "USA" to "us",
            "UK" to "gb",
            "Germany" to "de",
            "France" to "fr",
            "India" to "in",
            "Saudi Arabia" to "sa",
            "UAE" to "ae"
        )

        val countryNames = countries.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countryNames)
        binding.spinnerCountry.adapter = adapter

        val savedCountry = sharedPreferences.getString("country_code", "") ?: ""
        val savedIndex = countryNames.indexOfFirst { countries[it] == savedCountry }
        var isUserSelection = false
        if (savedIndex >= 0) {
            binding.spinnerCountry.setSelection(savedIndex)
            binding.spinnerCountry.post {
                isUserSelection = true
            }
        }

        binding.spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isUserSelection) {
                    isUserSelection = false
                    return
                }
                isUserSelection = true
                val selectedCountryName = countryNames[position]
                val selectedCountryCode = countries[selectedCountryName] ?: ""

                if (selectedCountryCode.isNotEmpty()) {
                    sharedPreferences.edit().putString("country_code", selectedCountryCode).apply()
                    Toast.makeText(this@SettingsActivity, "Country set to: $selectedCountryName", Toast.LENGTH_SHORT).show()
restartApp()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.switchDarkMode.isChecked = isDarkModeEnabled(this)

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            setDarkModeEnabled(this, isChecked)
            restartApp()
        }
    binding.btnLogout.setOnClickListener {

        showLogoutDialog()
    }
        binding.acc.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
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
    private fun restartApp() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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
