package ru.exursion.data.routes.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.AudioLocationDto
import ru.exursion.data.models.Review
import ru.exursion.data.models.ReviewDto
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import javax.inject.Inject

class RouteDetailsMapper @Inject constructor(
    private val tagsMapper: Mapper<TagDto, Tag>,
    private val locationsMapper: Mapper<AudioLocationDto, AudioLocation>,
    private val reviewMapper: Mapper<ReviewDto, Review>
) : Mapper<RouteDetailsDto, RouteDetails> {

    override fun map(input: RouteDetailsDto) = RouteDetails(
        id = input.id ?: -1,
        name = input.name ?: "",
        description = input.description ?: "",
        kilometers = input.kilometers ?: 0.0,
        durationInMinutes = input.durationInMinutes ?: 0,
        tags = tagsMapper.mapList(input.tags?.filterNotNull() ?: emptyList()),
        locations = locationsMapper.mapList(input.locations?.filterNotNull() ?: emptyList()),
        price = input.price ?: 0,
        isPaid = true,
        image = input.imageUrl ?: "",
        isFavorite = input.isFavorite ?: false,
        reviews = reviewMapper.mapList(input.reviews?.filterNotNull() ?: emptyList())
    )

}