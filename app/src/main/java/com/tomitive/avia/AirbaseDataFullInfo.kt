package com.tomitive.avia

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TabHost
import androidx.databinding.DataBindingUtil
import com.tomitive.avia.databinding.ActivityAirbaseDataFullInfoBinding
import com.tomitive.avia.model.TimeFormatManager
import com.tomitive.avia.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_airbase_data_full_info.*
import kotlinx.android.synthetic.main.activity_airbase_data_full_info.view.*
import kotlinx.android.synthetic.main.avia_toolbar.view.*

class AirbaseDataFullInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityAirbaseDataFullInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_airbase_data_full_info)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        val airportFullName = intent.getStringExtra("airbaseFullName")?: "Unknown airport"
        val airportName = intent.getStringExtra("airbaseName")?: "Unknown airport"

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Searching for updates", Snackbar.LENGTH_LONG).show()
        }
        viewPager.currentItem = 1

        val format = "EEEE dd/MM/yyyy HH:mm:ss (UTC)"
        val timeZone = "GMT+000"

        binding.airportName = airportFullName
        binding.timeBar.timezone =
            TimeFormatManager(format, timeZone)

    }
}