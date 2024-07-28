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

    fun getRoutesByCity(cityId: Long, tagId: Long): Single<List<Route>>
    fun getRouteDetailsWithComments(
        routeId: Long,
        amountOfReviews: Int = DEFAULT_REVIEWS_AMOUNT
    ): Single<Result<RouteDetailsWithReview>>
}

class RoutesUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository,
    private val reviewsRepository: ReviewsRepository,
) : RoutesUseCase {

    override fun getRoutesByCity(cityId: Long, tagId: Long): Single<List<Route>> {
        return locationsRepository.getRoutesByTag(tagId)
            .map { it.getOrThrow().filter { it.city == cityId } }
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