package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Audio
import ru.exursion.data.models.AudioDto
import javax.inject.Inject

class AudioMapper @Inject constructor(): Mapper<AudioDto, Audio> {

    override fun map(input: AudioDto) = Audio(
        input.id ?: -1,
        input.name ?: "",
        input.audioUrl ?: "",
        input.locationId ?: -1,
        input.durationInSeconds ?: 0
    )
}