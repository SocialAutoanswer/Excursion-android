package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.City
import javax.inject.Inject

interface CitiesUseCase {

    fun getCities(): Flowable<PagingData<City>>
    fun getCitiesPage(page: Int): Single<List<City>>
}

class CitiesUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository,
) : CitiesUseCase {

    override fun getCities(): Flowable<PagingData<City>> {
        return locationsRepository.getCities()
    }

    override fun getCitiesPage(page: Int): Single<List<City>> {
        return locationsRepository.getCitiesPage(page)
            .map { it.getOrThrow() }
            .observeOn(AndroidSchedulers.mainThread())
    }
}