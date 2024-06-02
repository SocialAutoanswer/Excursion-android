package ru.exursion.data.locations.paging

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.DefaultRxPagingSource
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDto
import ru.exursion.data.network.ExcursionApi

class RoutesPagingSource @AssistedInject constructor(
    private val api: ExcursionApi,
    private val routesMapper: Mapper<RouteDto, Route>,
    @Assisted("cityId") private val city: Long,
) : DefaultRxPagingSource<Route>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Route>> {
        val pageNumber = params.key ?: 1

        return api.requestRoutesByCity(city, pageNumber)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.isSuccessful) {

                    val pageDto = response.body()

                    val data = pageDto?.routes?.let {
                        routesMapper.mapList(it.filterNotNull())
                    } ?: emptyList()

                    val nextPageNumber = if (pageDto?.nextPage == null) null else pageNumber.inc()
                    val previousPageNumber = if (pageDto?.previousPage == null) null else pageNumber.dec()

                    LoadResult.Page(data, previousPageNumber, nextPageNumber)
                } else {
                    LoadResult.Error(CanNotGetDataException())
                }
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("cityId") cityId: Long): RoutesPagingSource
    }
}