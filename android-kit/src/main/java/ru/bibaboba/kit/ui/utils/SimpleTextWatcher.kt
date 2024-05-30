package ru.bibaboba.kit.ui.utils

import android.text.Editable
import android.text.TextWatcher

fun interface SimpleTextWatcher: TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable?) {}

}