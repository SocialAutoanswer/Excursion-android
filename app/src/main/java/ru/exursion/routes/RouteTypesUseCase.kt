package ru.exursion.routes

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.exursion.shared.ui.SelectItem

class RouteTypesUseCase {

    fun getRouteTypes() = Single.fromCallable {
        Thread.sleep(2000L)
        listOf(
            SelectItem(null, "Type of town"),
            SelectItem(null, "Fucking towns")
        )
    }.subscribeOn(Schedulers.io())
}