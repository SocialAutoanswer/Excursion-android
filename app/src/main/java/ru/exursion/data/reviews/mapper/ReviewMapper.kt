package ru.exursion.data.reviews.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import java.time.LocalDateTime
import javax.inject.Inject

class ReviewMapper @Inject constructor(): Mapper<ReviewDto, Review> {

    override fun map(input: ReviewDto) = Review(
        id = input.id ?: -1,
        reviewText = input.reviewText ?: "",
        createdAt = LocalDateTime.parse(input.createdAt) ?: LocalDateTime.now(),
        rating = input.rating ?: 0,
        userId = input.userId ?: -1,
        locationId = input.locationId ?: -1,
        routeId = input.routeId ?: -1,
    )

    override fun mapList(input: List<ReviewDto>): List<Review> {
        return input.map { map(it) }
    }
}