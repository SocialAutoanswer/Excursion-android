package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.SocialMedia
import ru.exursion.data.models.SocialMediaDto
import javax.inject.Inject

class SocialMediaMapper @Inject constructor(): Mapper<SocialMediaDto, SocialMedia> {

    override fun map(input: SocialMediaDto) = SocialMedia(
        input.id ?: -1,
        input.name ?: "",
        input.socialUrl ?: "",
        input.iconName ?: ""
    )
}