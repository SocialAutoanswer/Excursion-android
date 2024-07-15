package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import javax.inject.Inject

class LocationsMapper @Inject constructor() : Mapper<LocationDto, Location> {
    override fun map(input: LocationDto) = Location(
        id = input.id ?: -1,
        name = input.name ?: "",
        description = input.description ?: "",
        longitude = input.longitude ?: 0.0,
        latitude = input.latitude ?: 0.0,
        cityId = input.cityId ?: 0L,
        routesIds = input.routes?.filterNotNull() ?: emptyList()
    )

    override fun mapList(input: List<LocationDto>): List<Location> {
        return input.map(::map)
    }
}