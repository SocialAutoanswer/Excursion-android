package ru.exursion.data.routes.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ReviewMapper @Inject constructor(): Mapper<ReviewDto, Review> {

    override fun map(input: ReviewDto) = Review(
        id = input.id ?: -1,
        reviewText = input.reviewText ?: "",
        createdAt = LocalDateTime.parse(input.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME) ?: LocalDateTime.now(),
        rating = input.rating ?: 0,
        userName = input.userName ?: ""
    )
}