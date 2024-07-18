package ru.exursion.data.reviews

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.InternalServerException
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

interface ReviewsRepository {
    fun getRouteReviews(routeId: Long): Single<Result<List<Review>>>
}

class ReviewsRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val reviewMapper: Mapper<ReviewDto, Review>,
) : ReviewsRepository {

    override fun getRouteReviews(routeId: Long): Single<Result<List<Review>>> {
        return api.requestRouteReviews(routeId)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(reviewMapper.mapList(dto))
                } else {
                    when(it.code()) {
                        500 -> Result.failure(InternalServerException())
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
    }
}