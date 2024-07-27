package ru.exursion.domain.ext

import android.app.Service
import ru.exursion.App

inline fun <reified S : Service> S.inject() {
    val app = (applicationContext as App?) ?: return
    val appComponent = app.appComponent ?: return

    val inject = appComponent::class.java.getMethod("inject", this::class.java)
    inject.invoke(appComponent, this)
}