package ru.exursion

import androidx.fragment.app.Fragment

fun Fragment.showLoader() = LoadingDialog().show(parentFragmentManager, "LOADING_DIALOG")

fun Fragment.dismissLoader() =
    (parentFragmentManager.findFragmentByTag("LOADING_DIALOG") as LoadingDialog).let { loader ->
        loader.dismiss()
    }


fun Fragment.navigateToApp(navigator: AppNavigator) {
    navigator.navigate(::startActivity)
}