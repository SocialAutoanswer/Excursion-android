package ru.bibaboba.kit.util

import android.text.Editable
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.EditText
import ru.bibaboba.kit.ui.utils.SimpleTextWatcher
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter


const val PASSWORD_MIN_LENGTH = 8

fun Long.toTimeFormat(): String = DateTimeFormatter.ofPattern("mm:ss")
    .withZone(ZoneId.of("UTC"))
    .format(Instant.ofEpochMilli(this))


fun CharSequence.isValidEmail() = isNotEmpty() && EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence.isValidPassword() = isNotEmpty() && this.length > PASSWORD_MIN_LENGTH

fun EditText.addTextChangedListener(listener: SimpleTextWatcher) {
    this.addTextChangedListener(listener)
}

fun Editable.toInt(): Int = this.toString().toInt()