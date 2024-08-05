package ru.exursion.domain

import io.reactivex.rxjava3.core.Single
import ru.exursion.data.models.Location
import ru.exursion.data.models.Route
import javax.inject.Inject

interface ContentUseCase {
    fun getRoutesByCity(cityId: Long, tagId: Long): Single<List<Route>>
    fun getLocationsByCity(cityId:Long) : Single<List<Location>>
}

class ContentUseCaseImpl @Inject constructor(
    private val locationUseCase: LocationUseCase,
    private val routesUseCase: RoutesUseCase
): ContentUseCase {

    override fun getRoutesByCity(cityId: Long, tagId: Long): Single<List<Route>> =
        routesUseCase.getRoutesByCity(cityId, tagId)

    override fun getLocationsByCity(cityId: Long): Single<List<Location>> =
        locationUseCase.getLocationsByCity(cityId)
}
