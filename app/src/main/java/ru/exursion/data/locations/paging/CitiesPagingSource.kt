package ru.exursion.data.locations.paging

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.DefaultRxPagingSource
import ru.exursion.data.network.ExcursionApi
import ru.exursion.data.models.City
import ru.exursion.data.models.CityDto
import javax.inject.Inject

class CitiesPagingSource @Inject constructor(
    private val api: ExcursionApi,
    private val citiesMapper: Mapper<CityDto, City>
) : DefaultRxPagingSource<City>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, City>> {
        val pageNumber = params.key ?: 1

        return api.requestCitiesPage(pageNumber)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.isSuccessful) {

                    val pageDto = response.body()

                    val data = pageDto?.cities?.let {
                        citiesMapper.mapList(it.filterNotNull())
                    } ?: emptyList()

                    val nextPageNumber = if (pageDto?.nextPageLink == null) null else pageNumber.inc()
                    val previousPageNumber = if (pageDto?.previousPageLink == null) null else pageNumber.dec()

                    LoadResult.Page(data, previousPageNumber, nextPageNumber)
                } else {
                    LoadResult.Error(CanNotGetDataException())
                }
            }
    }
}