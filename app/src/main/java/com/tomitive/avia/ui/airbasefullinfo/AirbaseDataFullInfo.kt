package com.tomitive.avia.ui.airbasefullinfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.tomitive.avia.R
import com.tomitive.avia.databinding.ActivityAirbaseDataFullInfoBinding
import com.tomitive.avia.model.TimeFormatManager
import com.tomitive.avia.model.airports
import com.tomitive.avia.ui.main.SectionsPagerAdapter
import com.tomitive.avia.utils.MetarManager
import com.tomitive.avia.utils.TimeManager

class AirbaseDataFullInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAirbaseDataFullInfoBinding =
            DataBindingUtil.setContentView(this,
                R.layout.activity_airbase_data_full_info
            )

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        val airportFullName = intent.getStringExtra("airbaseFullName") ?: "Unknown airport"
        val airportName = intent.getStringExtra("airbaseName") ?: "Unknown airport"

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Searching for updates", Snackbar.LENGTH_LONG).show()
            val newMetar = MetarManager.getForecast(airportName)
            if (newMetar == null) {
                Snackbar.make(
                    view,
                    "Unable to connect. Check your internet connection",
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            airports.find { it.airportName == airportName }?.apply {
                timestamp = TimeManager.time
                metar = newMetar
            }
        }
        viewPager.currentItem = 1

        val format = "EEEE dd/MM/yyyy HH:mm:ss (UTC)"
        val timeZone = "GMT+000"

        binding.airportName = airportFullName
        binding.timeBar.timezone =
            TimeFormatManager(format, timeZone)

    }
}