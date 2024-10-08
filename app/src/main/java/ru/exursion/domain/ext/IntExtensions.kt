package ru.exursion.domain.ext

import android.content.Context
import android.util.TypedValue

fun Int.toDpPixels(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    ).toInt()
}