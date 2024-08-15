package ru.exursion.ui.shared.ext

fun Long?.isValidId() = (this != null && this > 0)