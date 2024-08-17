package ru.exursion.data.routes.paging

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.DefaultRxPagingSource
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.network.ExcursionApi

class ReviewsPagingSource @AssistedInject constructor(
    private val api: ExcursionApi,
    private val reviewMapper: Mapper<ReviewDto, Review>,
    @Assisted("routeId") private val routeId: Long
): DefaultRxPagingSource<Review>()  {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Review>> {
        val pageNumber = params.key ?: 1

        return api.requestRouteReviews(routeId)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.isSuccessful) {

                    val pageDto = response.body()

                    val data = pageDto?.data?.let {
                        reviewMapper.mapList(it.filterNotNull())
                    } ?: emptyList()

                    val nextPageNumber = if (pageDto?.nextPageLink == null) null else pageNumber.inc()
                    val previousPageNumber = if (pageDto?.previousPageLink == null) null else pageNumber.dec()

                    LoadResult.Page(data, previousPageNumber, nextPageNumber)
                } else {
                    handleHttpErrors(response)
                }
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("routeId") routeId: Long): ReviewsPagingSource
    }


}