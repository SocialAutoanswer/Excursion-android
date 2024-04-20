package ru.exursion.shared.ui.dialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Fragment.dialog(tag: String, lambda: DialogDslPackager.() -> Unit) = dialog(parentFragmentManager, tag, lambda)

fun dialog(fragmentManager: FragmentManager, tag: String, lambda: DialogDslPackager.() -> Unit) {
    DialogDslPackager().apply(lambda)
        .pack()
        .show(fragmentManager, tag)
}