package ru.exursion.ui.shared.ext

import androidx.annotation.ColorRes
import androidx.recyclerview.widget.RecyclerView
import ru.exursion.ui.shared.DividerItemDecoration
import ru.exursion.ui.shared.MarginItemDecoration

fun RecyclerView.addItemMargins(horizontal: Int = 0, vertical: Int = 0) {
    addItemDecoration(MarginItemDecoration(vertical, horizontal))
}

fun RecyclerView.addItemDivider(@ColorRes color: Int, dividerHeight: Int = 3) {
    addItemDecoration(DividerItemDecoration(color, dividerHeight))
}