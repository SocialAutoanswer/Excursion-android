package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.City
import ru.exursion.data.models.CityDto
import javax.inject.Inject

class CitiesMapper @Inject constructor() : Mapper<CityDto, City> {

    override fun map(input: CityDto) = City(
        id = input.id ?: -1,
        name = input.name ?: "",
        image = input.photo ?: "",
        longitude = input.longitude,
        latitude = input.latitude,
    )
}