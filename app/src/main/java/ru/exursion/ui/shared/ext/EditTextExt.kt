package ru.exursion.ui.shared.ext

import android.widget.EditText
import ru.bibaboba.kit.ui.getColorByAttr
import ru.bibaboba.kit.ui.utils.SimpleTextWatcher
import ru.exursion.R

fun EditText.addTextChangedListener(listener: SimpleTextWatcher) {
    this.addTextChangedListener(listener)
}

fun EditText.setErrorState() {
    val errorColor = this.context.getColorByAttr(R.attr.exc_input_error_color)
    this.setBackgroundResource(R.drawable.background_edit_error)
    this.setTextColor(errorColor)
}

fun EditText.setDefaultState() {
    val errorColor = this.context.getColorByAttr(R.attr.exc_text_color_main)
    this.setBackgroundResource(R.drawable.background_edit_text)
    this.setTextColor(errorColor)
}

