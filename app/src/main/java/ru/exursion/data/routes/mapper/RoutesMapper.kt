package ru.exursion.data.routes.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.BuildConfig
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
        isPaid = (input.price ?: 0) != 0,
        imageUrl = (BuildConfig.EXC_URL + input.imageUrl),
        cityId = input.locations?.filterNotNull()?.get(0)?.cityId ?: -1
    )
}