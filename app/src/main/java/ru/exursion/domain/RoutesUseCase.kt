package ru.exursion.domain

import android.util.Log
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
        tagId: Long,
        amountOfReviews: Int = DEFAULT_REVIEWS_AMOUNT
    ): Single<RouteDetailsWithReview>
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
        tagId: Long,
        amountOfReviews: Int,
    ): Single<RouteDetailsWithReview> {
        return Single.zip(
            locationsRepository.getRoutesByTag(tagId),
            reviewsRepository.getRouteReviewsFirstPage(routeId)
        ) { routes, reviews ->
            RouteDetailsWithReview(
                routes.getOrThrow().find { it.id == routeId } ?: throw CanNotGetDataException(),
                reviews.getOrNull() ?: emptyList()
            )
        }
    }
}