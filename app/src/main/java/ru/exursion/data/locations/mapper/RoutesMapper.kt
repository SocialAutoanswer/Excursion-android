package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDto
import javax.inject.Inject

class RoutesMapper @Inject constructor() : Mapper<RouteDto, Route> {

    override fun map(input: RouteDto) = Route(
        id = input.id ?: -1,
        name = input.name ?: "",
        description = input.description ?: "",
        kilometers = input.kilometers ?: 0.0,
        durationInMinutes = input.duration ?: 0,
        tags = input.tagIds?.filterNotNull() ?: emptyList(),
        isPaid = false,
        image = "https://union-travel.ru/assets/images/ekskurs/school-bus.jpg"
    )
}