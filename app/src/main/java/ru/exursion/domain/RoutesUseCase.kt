package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.Tag
import ru.exursion.data.routes.RoutesRepository
import javax.inject.Inject

interface RoutesUseCase {
    fun getRoutes(cityId: Long? = null, tagId: Long? = null): Flowable<PagingData<Route>>
    fun getRouteById(routeId: Long): Single<RouteDetails>
    fun getRoutesTags(): Flowable<PagingData<Tag>>
}

class RoutesUseCaseImpl @Inject constructor(
    private val routesRepository: RoutesRepository
) : RoutesUseCase {

    override fun getRoutes(cityId: Long?, tagId: Long?): Flowable<PagingData<Route>> {
        return routesRepository.getRoutes(
            cityId = cityId,
            tagId = tagId
        )
    }

    override fun getRouteById(routeId: Long): Single<RouteDetails> {
        return routesRepository.getRouteById(routeId)
            .map { result ->
                result.getOrThrow()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getRoutesTags(): Flowable<PagingData<Tag>> {
        return routesRepository.getRoutesTags()
    }
}