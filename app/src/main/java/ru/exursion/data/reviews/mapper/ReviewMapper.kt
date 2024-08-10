package ru.exursion.data.reviews.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.models.User
import ru.exursion.data.models.UserDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ReviewMapper @Inject constructor(
    private val userMapper: Mapper<UserDto, User>
): Mapper<ReviewDto, Review> {

    override fun map(input: ReviewDto) = Review(
        id = input.id ?: -1,
        reviewText = input.reviewText ?: "",
        createdAt = LocalDateTime.parse(input.createdAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME) ?: LocalDateTime.now(),
        rating = input.rating ?: 0,
        user = input.user?.let { userMapper.map(it) }
    )
}