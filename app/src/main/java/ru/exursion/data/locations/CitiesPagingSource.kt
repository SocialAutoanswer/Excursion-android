package ru.exursion.data.locations

import android.annotation.SuppressLint
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import dagger.assisted.AssistedInject
import io.reactivex.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import ru.bibaboba.kit.util.Mapper
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

    @SuppressLint("CheckResult")
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, City>> {
        api.requestCitiesPage(params.key ?: 1)
            .observeOn(Schedulers.io())
            .map { response ->
                val nextPageNumber =
                if (response.isSuccessful) {
                    LoadResult.Page(
                        response.body()?.cities?.let {
                            citiesMapper.mapList(it.filterNotNull())
                        } ?: emptyList(),
                        1, 1
                    )
                } else {

                }
            }
    }
}