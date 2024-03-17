package ru.exursion

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Fragment.showLoader() = LoadingDialog().show(parentFragmentManager, "LOADING_DIALOG")
