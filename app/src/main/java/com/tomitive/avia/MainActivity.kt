package com.tomitive.avia

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tomitive.avia.databinding.ActivityMainBinding
import com.tomitive.avia.interfaces.NavControllerReselectedListener
import com.tomitive.avia.interfaces.NavControllerSelectedListener
import com.tomitive.avia.model.Airport
import com.tomitive.avia.model.TimeFormatManager
import com.tomitive.avia.model.airports
import com.tomitive.avia.utils.airportLocation
import com.tomitive.avia.utils.airportName
import me.ibrahimsn.lib.SmoothBottomBar


class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var navView: SmoothBottomBar
    private lateinit var navController: NavController
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


        val format = "EEEE dd/MM/yyyy HH:mm:ss (UTC)"
        val timeZone = "GMT+000"

        binding.timeBar.timezone =
            TimeFormatManager(format, timeZone)
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
        val sharedPreferences = getSharedPreferences("shared preferences", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("airport list", null)
        val type = object : TypeToken<List<Airport>>() {}.type

        airports = gson.fromJson(json, type) ?: emptyList()

        if (airports.isEmpty())
            airports = airportName.map { Airport(it.key, it.value, airportLocation[it.key]) }
    }
}