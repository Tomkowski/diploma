package com.tomitive.avia.interfaces

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.whenResumed
import com.tomitive.avia.R
import com.tomitive.avia.ui.reservations.ReservationsFragment
import com.tomitive.avia.ui.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.runBlocking
import me.ibrahimsn.lib.OnItemSelectedListener
import kotlin.concurrent.thread

/**
 * Listener reagujący na wybranie elementu z dolnego menu nawigacji
 *
 * @property parentActivity aktywność, w której znajduje się menu nawigacji
 */
class NavControllerSelectedListener(private val parentActivity: AppCompatActivity) :
    OnItemSelectedListener {

    /**
     * metoda odpowiadająca za logikę wciśnięcia elementu z dolnego paska nawigacji
     *
     * @param pos pozycja elementu w menu nawigacji
     */
    override fun onItemSelect(pos: Int): Boolean {
        if (isLoading) return true

        val fragmentToLaunch = when (pos) {
            1 -> {
                Log.d(TAG, "FAVS IS SELECTED")
                R.id.navigation_reservations
            }
            0 -> {
                Log.d(TAG, "SEARCH IS SELECTED")
                R.id.navigation_search
            }
            else -> {
                Log.d(TAG, "Invalid id - $pos")
                return true
            }
        }

        setProgressBarVisible(true)
        launchFragment(fragmentToLaunch)

        return true

    }
    private val TAG = "NavSelectedListener"

    /**
     * wyświetla widok ładowania układu
     *
     * @param flag czy widok ładowania ma być widoczny
     */
    private fun setProgressBarVisible(flag: Boolean) {
        parentActivity.progress_bar.visibility = if (flag) View.VISIBLE else View.INVISIBLE
        Log.d(TAG, "visibility: ${parentActivity.progress_bar.isVisible} and flag: $flag")
    }

    /**
     * uruchamia wybrany fragment
     *
     * @param fragmentId ID fragmentu
     */
    private fun launchFragment(fragmentId: Int) {
        thread {
            runBlocking {
                isLoading = true
                val fragment: Fragment? = when (fragmentId) {
                    R.id.navigation_search -> SearchFragment()
                    R.id.navigation_reservations -> ReservationsFragment()
                    else -> null
                }
                fragment?.run {
                    parentActivity
                        .supportFragmentManager
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.nav_host_fragment, this, "fragment")
                        .commit()
                }
                fragment?.whenResumed {
                    isLoading = false
                    setProgressBarVisible(false)
                }

            }
        }
    }

    /**
     * flaga ładowania układu dostępna statycznie
     */
    companion object {
        var isLoading = false
    }
}