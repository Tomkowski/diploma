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
import com.google.android.gms.maps.MapsInitializer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tomitive.avia.api.LoginActivity
import com.tomitive.avia.api.RestApiService
import com.tomitive.avia.databinding.ActivityMainBinding
import com.tomitive.avia.interfaces.NavControllerReselectedListener
import com.tomitive.avia.interfaces.NavControllerSelectedListener
import com.tomitive.avia.model.*
import kotlinx.android.synthetic.main.avia_toolbar.*
import me.ibrahimsn.lib.SmoothBottomBar
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    private lateinit var navView: SmoothBottomBar
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        MapsInitializer.initialize(this)
        super.onCreate(savedInstanceState)

        intent.extras?.run {
            when(getString("status")) {
                "200" ->{
                    downloadReservations()
                    Log.d("intent_main", "called for download")
                }
                "414" -> {
                    Toast.makeText(this@MainActivity, getString(R.string.end_session), Toast.LENGTH_SHORT).show()
                    logout()
                }
                else -> {
                    Toast.makeText(this@MainActivity, getString(R.string.reservation_failed), Toast.LENGTH_LONG).show()
                }
            }
        }
        loadData()


        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        navView = findViewById(R.id.nav_view)

        with(navView) {
            onItemSelectedListener = NavControllerSelectedListener(this@MainActivity)
            onItemReselectedListener = NavControllerReselectedListener(this@MainActivity)

        }
        binding.usernameBar.username = fetchUsername()
        logout_button.setOnClickListener {
            logout()
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
        val json = gson.toJson(reservations)
        editor.putString("reservation list", json)
        editor.apply()
        Log.d("mainActivity", "data saved!")
    }

    private fun loadData() {
        val sharedPreferences =
            getSharedPreferences(getString(R.string.preferencesName), Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("reservation list", null)
        val type = object : TypeToken<List<Reservation>>() {}.type
        if(reservations.isEmpty())
            reservations = gson.fromJson(json, type)?: mutableListOf()

        if(reservations.isEmpty()){
            downloadReservations()
        }
    }

    fun logout() {
        reservations = mutableListOf()
        saveData()

        val service = RestApiService()
        service.logout(Credentials(fetchUsername(), fetchToken())) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun downloadReservations(){
        thread {
            val service = RestApiService()
            service.fetchAllReservations(Credentials(fetchUsername(), fetchToken())) {
                reservations = if (it == null) mutableListOf() else it as MutableList<Reservation>
                Log.d("debug", "assigned")
            }
        }.join()
    }

    fun fetchUsername(): String{
        return getSharedPreferences(
            getString(R.string.preferencesName),
            Context.MODE_PRIVATE
        ).getString(getString(R.string.sharedUsername), "default") ?: "empty"
    }

    fun fetchToken(): String{
        return getSharedPreferences(
            getString(R.string.preferencesName),
            Context.MODE_PRIVATE
        ).getString(getString(R.string.sharedPassword), "default") ?: "empty"
    }
}