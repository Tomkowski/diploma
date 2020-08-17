package com.tomitive.avia.interfaces

import android.content.Context.INPUT_METHOD_SERVICE
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.tomitive.avia.R
import com.tomitive.avia.ui.map.MapFragment
import me.ibrahimsn.lib.OnItemReselectedListener


class NavControllerReselectedListener(private val parentActivity: AppCompatActivity) :
        OnItemReselectedListener {
    private val TAG = "navControllerListener"

    private fun restartMapFragment() {
        val fragment = parentActivity.supportFragmentManager.findFragmentByTag("fragment") as MapFragment

        fragment.refreshMap()
        fragment.hideToolbar()
    }

    private fun openSoftKeyboard() {
        val searchEditText =
            parentActivity.findViewById<EditText>(R.id.navigation_search_edit_text)
        searchEditText.requestFocus()
        val imm =
            parentActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    companion object{
        var isLoading = false
    }

    override fun onItemReselect(pos: Int) {
        if(NavControllerSelectedListener.isLoading) return

        when (pos) {
            2 -> {
                restartMapFragment()
                Log.d(TAG, "MAP IS RESELECTED")
            }

            1 -> Log.d(TAG, "FAVS IS RESELECTED")

            0 -> {
                openSoftKeyboard()
                Log.d(TAG, "SEARCH IS RESELECTED")
            }
        }
    }
}