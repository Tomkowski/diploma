package com.tomitive.avia.utils

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewCenterItemListener : RecyclerView.OnScrollListener() {
    private val TAG = "RvScrollListener"

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        when(newState){
            RecyclerView.SCROLL_STATE_IDLE -> {
                val manager = recyclerView.layoutManager as LinearLayoutManager

                with(manager) {
                    val position = findFirstCompletelyVisibleItemPosition()
                    recyclerView.smoothScrollToPosition(position)
                }
            }
        }
    }
}