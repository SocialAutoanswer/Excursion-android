package ru.exursion.domain

import androidx.paging.PagingData
import androidx.paging.filter
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDetailsWithReview
import ru.exursion.data.reviews.ReviewsRepository
import javax.inject.Inject

interface RoutesUseCase {

    companion object {
        const val DEFAULT_REVIEWS_AMOUNT = 2
    }

    fun getRoutesByCity(cityId: Long, tagId: Int): Flowable<PagingData<Route>>
    fun getRouteDetailsWithComments(
        routeId: Long,
        amountOfReviews: Int = DEFAULT_REVIEWS_AMOUNT
    ): Single<Result<RouteDetailsWithReview>>
}

class RoutesUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository,
    private val reviewsRepository: ReviewsRepository,
) : RoutesUseCase {

    override fun getRoutesByCity(cityId: Long, tagId: Int): Flowable<PagingData<Route>> {
        return locationsRepository.getRoutesByCity(cityId)
            .map { it.filter { it.tags.contains(tagId) } }
    }

    override fun getRouteDetailsWithComments(
        routeId: Long,
        amountOfReviews: Int,
    ): Single<Result<RouteDetailsWithReview>> {
        return locationsRepository.getRouteDetails(routeId)
            .zipWith(reviewsRepository.getRouteReviews(routeId)) { routeDetails, reviews ->
                if (routeDetails.isFailure) {
                    return@zipWith Result.failure(
                        routeDetails.exceptionOrNull() ?: CanNotGetDataException()
                    )
                }

                Result.success(
                    RouteDetailsWithReview(
                        routeDetails.getOrThrow(),
                        reviews.getOrNull() ?: emptyList()
                    )
                )
            }

    }
}