package ru.exursion.data.locations

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.ExcursionApi
import ru.exursion.data.locations.models.City
import ru.exursion.data.locations.models.CityDto
import javax.inject.Inject

class CitiesPagingSource @Inject constructor(
    private val api: ExcursionApi,
    private val citiesMapper: Mapper<CityDto, City>
) : RxPagingSource<Int, City>() {

    override fun getRefreshKey(state: PagingState<Int, City>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, City>> {
        val pageNumber = params.key ?: 1

        return api.requestCitiesPage(pageNumber)
            .observeOn(Schedulers.io())
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
            .onErrorReturn { LoadResult.Error(it) }
    }
}