package ru.exursion.data.locations.mapper

import com.yandex.mapkit.geometry.Point
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Audio
import ru.exursion.data.models.AudioDto
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.AudioLocationDto
import ru.exursion.data.models.City
import ru.exursion.data.models.CityDto
import ru.exursion.data.models.Photo
import ru.exursion.data.models.PhotoDto
import javax.inject.Inject

class AudioLocationMapper @Inject constructor(
    private val cityMapper: Mapper<CityDto, City>,
    private val audioMapper: Mapper<AudioDto, Audio>,
    private val photoMapper: Mapper<PhotoDto, Photo>
): Mapper<AudioLocationDto, AudioLocation> {

    override fun map(input: AudioLocationDto): AudioLocation = AudioLocation(
        input.id ?: -1,
        input.name ?: "",
        input.description ?: "",
        input.isFavorite ?: false,
        cityMapper.map(input.city ?: CityDto(-1, "", "", "","")),
        Point(input.latitude ?: 0.0, input.longitude ?: 0.0),
        audioMapper.mapList(input.audios?.filterNotNull() ?: emptyList()),
        photoMapper.mapList(input.photos?.filterNotNull() ?: emptyList())
    )
}