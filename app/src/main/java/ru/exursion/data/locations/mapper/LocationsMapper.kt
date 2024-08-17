package ru.exursion.data.locations.mapper

import com.yandex.mapkit.geometry.Point
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import javax.inject.Inject

class LocationsMapper @Inject constructor() : Mapper<LocationDto, Location> {
    override fun map(input: LocationDto) = Location(
        id = input.id ?: -1,
        name = input.name ?: "",
        description = input.description ?: "",
        imageUrl = "https://union-travel.ru/assets/images/ekskurs/school-bus.jpg",
        point = Point(input.latitude ?: 0.0, input.longitude ?: 0.0),
        cityId = input.cityId ?: -1L
    )

    override fun mapList(input: List<LocationDto>): List<Location> {
        return input.map(::map)
    }
}