package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Hotel
import ru.exursion.data.models.HotelDto
import ru.exursion.data.models.SocialMedia
import ru.exursion.data.models.SocialMediaDto
import javax.inject.Inject

class HotelMapper @Inject constructor(
    private val socialMediaMapper: Mapper<SocialMediaDto, SocialMedia>
): Mapper<HotelDto, Hotel> {

    override fun map(input: HotelDto) = Hotel(
        input.id ?: -1,
        input.name ?: "",
        input.description ?: "",
        input.imageUrl ?: "",
        input.rating ?: 0.0f,
        input.address ?: "",
        socialMediaMapper.mapList(input.socialMedias?.filterNotNull() ?: emptyList()),
        input.phoneNumber ?: "",
        input.isFavorite ?: false
    )
}