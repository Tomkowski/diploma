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

/**
 * Główna aktywność aplikacji
 *
 */
class MainActivity : AppCompatActivity() {
    /**
     * flaga sprawdzająca czy przycisk powrotu został wciśnięty dwukrotnie
     */
    private var doubleBackToExitPressedOnce = false

    /**
     * dolny panel nawigacji
     */
    private lateinit var navView: SmoothBottomBar

    /**
     * metoda wywoływana przy stworzeniu aktywności
     *
     * @param savedInstanceState mapa klucz-wartość zapisanych danych w pamięci urządzenia
     */
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

    /**
     * Metoda wywołująca się po wciśnięciu przycisku cofania
     *
     */
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, getString(R.string.press_back_to_exit), Toast.LENGTH_SHORT).show()

        // po dwóch sekundach anuluj sprawdzenie
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    /**
     * metoda wywoływana po zatrzymaniu aplikacji (po zamknięciu lub zminimalizowaniu)
     */
    override fun onStop() {
        super.onStop()
        saveData()
    }

    /**
     * metoda zapisująca stan rezerwacji do pamięci urządzenia
     */
    private fun saveData() {
        val editor = getSharedPreferences("shared preferences", Context.MODE_PRIVATE).edit()
        val gson = Gson()
        val json = gson.toJson(reservations)
        editor.putString("reservation list", json)
        editor.apply()
        Log.d("mainActivity", "data saved!")
    }

    /**
     * metoda wczytująca stan rezerwacji z pamięci urządzenia
     */
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

    /**
     * metoda wywoływana po wciśnięciu przycisku wylogowania
     */
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

    /**
     * metoda pobierająca rezerwacje z serwera
     */
    fun downloadReservations(){
        thread {
            val service = RestApiService()
            service.fetchAllReservations(Credentials(fetchUsername(), fetchToken())) {
                reservations = if (it == null) mutableListOf() else it as MutableList<Reservation>
            }
        }.join()
        Log.d("main", "$reservations")
    }

    /**
     * metoda zwracająca login obecnie zalogowanego użytkownika
     */
    fun fetchUsername(): String{
        return getSharedPreferences(
            getString(R.string.preferencesName),
            Context.MODE_PRIVATE
        ).getString(getString(R.string.sharedUsername), "default") ?: "empty"
    }

    /**
     * metoda zwracająca token obecnie zalogowanego użytkownika
     */
    fun fetchToken(): String{
        return getSharedPreferences(
            getString(R.string.preferencesName),
            Context.MODE_PRIVATE
        ).getString(getString(R.string.sharedPassword), "default") ?: "empty"
    }
}