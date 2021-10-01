package com.tomitive.avia.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Klasa odpowiedzialan za stworzenie wizualnych odstępów pomiędzy elementami listy
 *
 * @property spaceHeight odstęp liczony w dp
 */
class MarginItemDecoration(@IdRes private val spaceHeight: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView,
                                state: RecyclerView.State) {
        with(outRect){
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceHeight
            }
            left =   2 * spaceHeight
            right = 2 * spaceHeight
            bottom = spaceHeight
        }
    }
}