package com.tomitive.avia.interfaces

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tomitive.avia.R
import com.tomitive.avia.ui.map.MapFragment


class NavControllerListener(private val parentActivity: AppCompatActivity) :
    BottomNavigationView.OnNavigationItemReselectedListener {
    private val TAG = "navControllerListener"
    override fun onNavigationItemReselected(item: MenuItem) {
        when (item.itemId) {
            R.id.navigation_map -> {
                Log.d(TAG, "MAP IS RESELECTED")
                val fragment = MapFragment()
                parentActivity
                    .supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.map_view, fragment)
                    .commit()
            }
            R.id.navigation_favourites -> Log.d(TAG, "FAVS IS RESELECTED")
            R.id.navigation_search -> {
                val searchEditText =
                    parentActivity.findViewById<EditText>(R.id.navigation_search_edit_text)
                searchEditText.requestFocus()
                val imm =
                    parentActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
                Log.d(TAG, "SEARCH IS RESELECTED")
            }
        }
    }
}