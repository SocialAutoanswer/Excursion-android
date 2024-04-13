package ru.exursion

import androidx.fragment.app.Fragment

fun Fragment.showLoader() = LoadingDialog().show(parentFragmentManager, "LOADING_DIALOG")
