package ru.excursion.playground

import android.util.Log

fun Int.toTimeFormat(): String {

    if(this < 0){
        return "00:00"
    }

    return String.format("%02d:%02d", (this / 3600 * 60 + ((this % 3600) / 60)), (this % 60))
}
