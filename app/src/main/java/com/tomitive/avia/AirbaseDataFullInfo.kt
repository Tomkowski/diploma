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
import com.tomitive.avia.ui.main.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_airbase_data_full_info.*
import kotlinx.android.synthetic.main.activity_airbase_data_full_info.view.*

class AirbaseDataFullInfo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airbase_data_full_info)
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        val fab: FloatingActionButton = findViewById(R.id.fab)

        val airportName = intent.getStringExtra("airbaseName")

        appBarLayout.title.text = airportName

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Searching for updates", Snackbar.LENGTH_LONG).show()
        }
        viewPager.currentItem = 1
    }
}