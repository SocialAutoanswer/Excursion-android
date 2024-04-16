package ru.exursion.routes

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class TownsUseCase {

    fun getTowns(): Single<List<TownItem>> {
        return Single.fromCallable {
            Thread.sleep(2000L)
            listOf(TownItem(1, "https://cdn.tripster.ru/thumbs2/3a59c510-57e0-11ee-a5b1-e2b100a0f340.1220x600.jpeg", "Казань"))
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}