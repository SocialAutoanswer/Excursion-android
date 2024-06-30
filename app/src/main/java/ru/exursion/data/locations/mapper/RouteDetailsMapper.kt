package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import javax.inject.Inject

class RouteDetailsMapper @Inject constructor(
    private val tagsMapper: Mapper<TagDto, Tag>,
    private val locationsMapper: Mapper<LocationDto, Location>
) : Mapper<RouteDetailsDto, RouteDetails> {

    override fun map(input: RouteDetailsDto) = RouteDetails(
        id = input.id ?: -1,
        name = input.name ?: "",
        description = input.description ?: "",
        kilometers = input.kilometers ?: 0.0,
        durationInMinutes = input.durationInMinutes ?: 0,
        tags = tagsMapper.mapList(input.tags?.filterNotNull() ?: emptyList()),
        locations = locationsMapper.mapList(input.locations?.filterNotNull() ?: emptyList()),
        isPaid = false,
        image = "https://union-travel.ru/assets/images/ekskurs/school-bus.jpg",
    )

}