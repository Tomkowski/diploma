package com.tomitive.avia

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tomitive.avia.databinding.ActivityMainBinding
import com.tomitive.avia.interfaces.NavControllerListener
import com.tomitive.avia.model.TimeFormatManager
import com.tomitive.avia.utils.TafManager
import io.github.mivek.model.TAF
import io.github.mivek.parser.TAFParser


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment))



        navView.setupWithNavController(navController)

        navView.setOnNavigationItemReselectedListener(NavControllerListener(this))

        val format = "EEEE dd/MM/yyyy HH:mm:ss (UTC)"
        val timeZone = "GMT+000"

        binding.timeBar.timezone =
            TimeFormatManager(format, timeZone)

    }
}