package ru.bibaboba.kit.util

import android.text.InputFilter
import android.text.Spanned
import org.threeten.bp.Year as CurrentYear

sealed class NumberEditTextFilter(private val range: IntRange) : InputFilter {

    init {
        require(range.first < range.last) { "Range must be rising" }
    }


    data object Day : NumberEditTextFilter(1..31)
    data object Month : NumberEditTextFilter(1..12)
    data object Year : NumberEditTextFilter(1..CurrentYear.now().value)

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            if (Integer.parseInt(dest.toString() + source.toString()) in range) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

}
