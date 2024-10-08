package ru.exursion.data.routes.mapper

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
        price = input.price ?: 0,
        isPaid = true,
        imageUrl = input.imageUrl ?: "",
        cityId = input.locations?.firstOrNull()?.city?.id ?: -1
    )
}