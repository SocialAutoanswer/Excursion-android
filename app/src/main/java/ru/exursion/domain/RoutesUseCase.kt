package ru.exursion.domain

import androidx.paging.PagingData
import androidx.paging.filter
import io.reactivex.rxjava3.core.Flowable
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.City
import ru.exursion.data.models.Route
import javax.inject.Inject

interface RoutesUseCase {

    fun getRoutesByCity(cityId: Long, tagId: Int): Flowable<PagingData<Route>>
}

class RoutesUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository,
) : RoutesUseCase {

    override fun getRoutesByCity(cityId: Long, tagId: Int): Flowable<PagingData<Route>> {
        return locationsRepository.getRoutesByCity(cityId)
            .map { it.filter { it.tags.contains(tagId) } }
    }

}