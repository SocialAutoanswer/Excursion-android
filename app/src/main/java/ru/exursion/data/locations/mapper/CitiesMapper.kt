package ru.exursion.data.locations.mapper

import com.yandex.mapkit.geometry.Point
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.City
import ru.exursion.data.models.CityDto
import ru.exursion.ui.shared.ext.DEFAULT_LATITUDE
import ru.exursion.ui.shared.ext.DEFAULT_LONGITUDE
import javax.inject.Inject

class CitiesMapper @Inject constructor() : Mapper<CityDto, City> {

    override fun map(input: CityDto) = City(
        id = input.id ?: -1,
        name = input.name ?: "",
        image = input.photo ?: "",
        point = Point(
            input.latitude?.toDouble() ?: DEFAULT_LATITUDE,
            input.longitude?.toDouble() ?: DEFAULT_LONGITUDE
        )
    )
}