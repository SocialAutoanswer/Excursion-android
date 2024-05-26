package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.City
import javax.inject.Inject

interface CitiesUseCase {

    fun getCities(): Flowable<PagingData<City>>

}

class CitiesUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository,
) : CitiesUseCase {

    override fun getCities(): Flowable<PagingData<City>> {
        return locationsRepository.getCities()
    }
}