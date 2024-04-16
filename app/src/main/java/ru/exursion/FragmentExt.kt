package ru.exursion

import androidx.fragment.app.Fragment

inline fun <reified F : Fragment> F.inject() {
    val app = (context?.applicationContext as App?) ?: return
    val appComponent = app.appComponent ?: return

    val inject = appComponent::class.java.getMethod("inject", this::class.java)
    inject.invoke(appComponent, this)
}