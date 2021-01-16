package com.tomitive.avia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tomitive.avia.api.LoginActivity
import com.tomitive.avia.api.RestApiService
import com.tomitive.avia.databinding.ActivityMainBinding
import com.tomitive.avia.interfaces.NavControllerReselectedListener
import com.tomitive.avia.interfaces.NavControllerSelectedListener
import com.tomitive.avia.model.Airport
import com.tomitive.avia.model.Credentials
import com.tomitive.avia.model.TimeFormatManager
import com.tomitive.avia.model.airports
import com.tomitive.avia.utils.airportLocation
import com.tomitive.avia.utils.airportName
import kotlinx.android.synthetic.main.avia_toolbar.*
import me.ibrahimsn.lib.SmoothBottomBar


class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var navView: SmoothBottomBar
    private lateinit var navController: NavController
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        loadData()

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        navView = findViewById(R.id.nav_view)

        with(navView) {
            onItemSelectedListener = NavControllerSelectedListener(this@MainActivity)
            onItemReselectedListener = NavControllerReselectedListener(this@MainActivity)

        }
        binding.usernameBar.username = username
        logout_button.setOnClickListener {
            val service = RestApiService()
            val password = getSharedPreferences(getString(R.string.preferencesName), Context.MODE_PRIVATE).getString(getString(R.string.sharedPassword), "default")?: "empty"

            service.logout(Credentials(username, password)){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    private fun saveData() {
        val editor = getSharedPreferences("shared preferences", Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val json = gson.toJson(airports)
        editor.putString("airport list", json)
        editor.apply()
        Log.d("mainActivity", "data saved!")
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences(getString(R.string.preferencesName), Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("airport list", null)
        val type = object : TypeToken<List<Airport>>() {}.type
        username = getSharedPreferences(getString(R.string.preferencesName), Context.MODE_PRIVATE).getString(getString(R.string.sharedUsername), "defualt")?: "empty"
        airports = gson.fromJson(json, type) ?: emptyList()

        if (airports.isEmpty())
            airports = airportName.map { Airport(it.key, it.value, airportLocation[it.key]) }
    }
}