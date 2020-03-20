package com.tomitive.avia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tomitive.avia.databinding.ActivityMainBinding
import com.tomitive.avia.model.TimeFormatManager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = Navigation.findNavController(findViewById(R.id.nav_host_fragment))



        navView.setupWithNavController(navController)

        navView.setOnNavigationItemReselectedListener { }

        val format = "EEEE dd/MM/yyyy HH:mm:ss (UTC)"
        val timeZone = "GMT+000"

        binding.timeBar.timezone =
            TimeFormatManager(format, timeZone)


        /*


        //TODO export to model and logic
        Thread {
            try {
                val doc = Jsoup.connect("http://awiacja.imgw.pl/index.php?product=taf_mil").get()
                val elementsHtml: Elements? = doc.getElementsByClass("forecast")
                elementsHtml?.let {
                    Log.d("ELEMENT", elementsHtml.text())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()


        */
    }
}