package ru.exursion.ui.shared.ext

import androidx.recyclerview.widget.RecyclerView
import ru.exursion.ui.shared.MarginItemDecoration

fun RecyclerView.addItemMargins(horizontal: Int = 0, vertical: Int = 0) {
    addItemDecoration(MarginItemDecoration(vertical, horizontal))
}