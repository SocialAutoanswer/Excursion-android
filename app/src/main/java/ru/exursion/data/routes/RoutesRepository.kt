package ru.exursion.data.routes

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.bibaboba.kit.util.createPagingDataFlowable
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.InternalServerException
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.Tag
import ru.exursion.data.network.ExcursionApi
import ru.exursion.data.network.createHttpError
import ru.exursion.data.routes.paging.ReviewsPagingSource
import ru.exursion.data.routes.paging.RouteTagsPagingSource
import ru.exursion.data.routes.paging.RoutesPagingSource
import javax.inject.Inject

interface RoutesRepository {
    fun getRoutes(cityId: Long? = null, tagId: Long? = null): Flowable<PagingData<Route>>
    fun getRoutesTags(): Flowable<PagingData<Tag>>
    fun getRouteById(routeId: Long): Single<Result<RouteDetails>>
    fun getRouteReviews(routeId: Long): Flowable<PagingData<Review>>
    fun postReview(routeId: Long, rating: Int, review: String): Single<Unit>
}

class RoutesRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val routesPagingSourceFactory: RoutesPagingSource.Factory,
    private val routeTagsPagingSourceFactory: RouteTagsPagingSource.Factory,
    private val reviewsPagingSourceFactory: ReviewsPagingSource.Factory,
    private val routeMapper: Mapper<RouteDetailsDto, RouteDetails>
) : RoutesRepository {

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }

    override fun getRoutes(cityId: Long?, tagId: Long?): Flowable<PagingData<Route>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) {
            routesPagingSourceFactory.create(
                cityId = cityId,
                tagId = tagId
            )
        }
    }

    override fun getRoutesTags(): Flowable<PagingData<Tag>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) { routeTagsPagingSourceFactory.create() }
    }

    override fun getRouteById(routeId: Long): Single<Result<RouteDetails>> {
        return api.getRouteById(routeId)
            .subscribeOn(Schedulers.io())
            .map {
                if(it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(routeMapper.map(dto))
                } else {
                    when(it.code()) {
                        500 -> Result.failure(InternalServerException())
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
    }

    override fun getRouteReviews(routeId: Long): Flowable<PagingData<Review>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) { reviewsPagingSourceFactory.create(routeId) }
    }

    override fun postReview(routeId: Long, rating: Int, review: String): Single<Unit> {
        return api.sendRouteReview(routeId, ReviewDto(id = null, rating = rating, reviewText = review, createdAt = null, userName = null))
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    return@map
                }

                if (it.code() == 400) {
                    throw IllegalArgumentException()
                }

                throw createHttpError(it)
            }
    }
}