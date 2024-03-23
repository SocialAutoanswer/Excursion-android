package ru.excursion.playground

import android.util.Log

fun Int.toTimeFormat(): String {

    if(this < 0){
        return "00:00"
    }
    val seconds = this / 1000

    return String.format("%02d:%02d", (seconds / 3600 * 60 + ((seconds % 3600) / 60)), (seconds % 60))
}
