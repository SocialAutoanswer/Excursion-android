package ru.exursion

import androidx.recyclerview.widget.RecyclerView
import ru.exursion.shared.ui.MarginItemDecoration

fun RecyclerView.addItemMargins(horizontal: Int = 0, vertical: Int = 0) {
    addItemDecoration(MarginItemDecoration(vertical, horizontal))
}