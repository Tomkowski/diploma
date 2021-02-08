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

    private fun openSoftKeyboard() {
        val searchEditText =
            parentActivity.findViewById<EditText>(R.id.navigation_search_edit_text)?: return
        searchEditText.requestFocus()
        val imm =
            parentActivity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onItemReselect(pos: Int) {
        if(NavControllerSelectedListener.isLoading) return

        when (pos) {

            1 -> Log.d(TAG, "FAVS IS RESELECTED")

            0 -> {
                openSoftKeyboard()
                Log.d(TAG, "SEARCH IS RESELECTED")
            }
        }
    }
}