package ru.exursion.shared.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.exursion.shared.ext.toDpPixels

class MarginItemDecoration(
    private val marginVertical: Int = 0,
    private val marginHorizontal: Int = 0,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = marginVertical.toDpPixels(view.context)
            }
            left = marginHorizontal.toDpPixels(view.context)
            right = marginHorizontal.toDpPixels(view.context)
            bottom = marginVertical.toDpPixels(view.context)
        }
    }
}