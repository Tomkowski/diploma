package com.tomitive.avia.interfaces

import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tomitive.avia.R
import com.tomitive.avia.ui.favourites.FavouritesFragment
import com.tomitive.avia.ui.map.MapFragment
import com.tomitive.avia.ui.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

class NavControllerSelectedListener(private val parentActivity: AppCompatActivity) :
    BottomNavigationView.OnNavigationItemSelectedListener {
    private val TAG = "NavSelectedListener"
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(isLoading) return false

        setProgressBarVisible(true)
        Log.d(TAG, "progress bar is now visible")
        when (item.itemId) {
            R.id.navigation_map -> {
                Log.d(TAG, "MAP IS SELECTED")
            }

            R.id.navigation_favourites -> {
                Log.d(TAG, "FAVS IS SELECTED")
            }

            R.id.navigation_search -> {
                Log.d(TAG, "SEARCH IS SELECTED")
            }
            else -> {

                Log.d(TAG, "Invalid id - ${item.itemId}")
                return false
            }
        }
        launchFragment(item.itemId)

        Log.d(TAG, "progress bar is now visible - ${parentActivity.progress_bar.isVisible}")
        return true
    }

    private fun setProgressBarVisible(flag: Boolean) {
        parentActivity.progress_bar.visibility = if (flag) View.VISIBLE else View.INVISIBLE
        Log.d(TAG, "visibility: ${parentActivity.progress_bar.isVisible} and flag: $flag")
    }

    private fun launchFragment(fragmentId: Int) {
        thread {
            runBlocking {
                isLoading = true
                val fragment: Fragment? = when (fragmentId) {
                    R.id.navigation_map -> MapFragment()
                    R.id.navigation_search -> SearchFragment()
                    R.id.navigation_favourites -> FavouritesFragment()
                    else -> null
                }
                //Thread.sleep(2000) //for testing purposes
                fragment?.run {
                    parentActivity
                        .supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.nav_host_fragment, this)
                        .commit()
                }
                isLoading = false
                setProgressBarVisible(false)

            }
        }
    }
    companion object{
        var isLoading = false
    }
}