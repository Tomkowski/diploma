package com.tomitive.avia.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tomitive.avia.AirbaseDataFullInfo
import com.tomitive.avia.R
import com.tomitive.avia.ui.fragments.ActivityAirbaseDataMetar
import com.tomitive.avia.ui.fragments.ActivityAirbaseDataNotam
import com.tomitive.avia.ui.fragments.ActivityAirbaseDataTaf

private val TAB_TITLES = arrayOf(
    R.string.airbase_full_data_tab1,
    R.string.airbase_full_data_tab2,
    R.string.airbase_full_data_tab3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val airportName =
            (context as AirbaseDataFullInfo).intent.getStringExtra("airbaseName")?: "EPDE"
        return when(position){
            0 -> ActivityAirbaseDataTaf.newInstance(airportName, "")
            1 -> ActivityAirbaseDataMetar.newInstance(airportName, "")
            else -> ActivityAirbaseDataNotam.newInstance(airportName, "")

        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 3
    }
}