package ru.exursion.data.locations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.InternalServerError
import ru.exursion.data.locations.paging.CitiesPagingSource
import ru.exursion.data.locations.paging.RoutesPagingSource
import ru.exursion.data.locations.paging.TagsPagingSource
import ru.exursion.data.models.City
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.Tag
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

interface LocationsRepository {

    fun getCities(): Flowable<PagingData<City>>
    fun getTags(): Flowable<PagingData<Tag>>
    fun getRoutesByCity(cityId: Long): Flowable<PagingData<Route>>
    fun getRouteDetails(routeId: Long): Single<Result<RouteDetails>>
}

class LocationsRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val routeDetailsMapper: Mapper<RouteDetailsDto, RouteDetails>,
    private val citiesPagingSource: CitiesPagingSource,
    private val tagsPagingSource: TagsPagingSource,
    private val routesPagingSourceFactory: RoutesPagingSource.Factory
) : LocationsRepository {

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }

    override fun getCities(): Flowable<PagingData<City>> {
        return createPagingDataFlowable { citiesPagingSource }
    }

    override fun getTags(): Flowable<PagingData<Tag>> {
        return createPagingDataFlowable { tagsPagingSource }
    }

    override fun getRoutesByCity(cityId: Long): Flowable<PagingData<Route>> {
        return createPagingDataFlowable { routesPagingSourceFactory.create(cityId) }
    }

    override fun getRouteDetails(routeId: Long): Single<Result<RouteDetails>> {
        return api.requestRouteDetailsById(routeId)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(routeDetailsMapper.map(dto))
                } else {
                    when(it.code()) {
                        500 -> Result.failure(InternalServerError())
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
    }

    private fun <K: Any, V: Any> createPagingDataFlowable(
        pageSize: Int = DEFAULT_PAGE_SIZE,
        pagingSourceFactory: () -> PagingSource<K, V>
    ): Flowable<PagingData<V>> {
        return Pager(
            PagingConfig(pageSize = pageSize),
            pagingSourceFactory = pagingSourceFactory
        ).flowable
    }
}