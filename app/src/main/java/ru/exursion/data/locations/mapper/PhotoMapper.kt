package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Photo
import ru.exursion.data.models.PhotoDto
import javax.inject.Inject

class PhotoMapper @Inject constructor(): Mapper<PhotoDto, Photo> {

    override fun map(input: PhotoDto) = Photo(
        input.id ?: -1
    )
}