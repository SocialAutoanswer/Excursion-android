package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Event
import ru.exursion.data.models.EventDto
import javax.inject.Inject

class EventMapper @Inject constructor() : Mapper<EventDto, Event> {

    override fun map(input: EventDto) = Event(
        input.id ?: -1,
        input.name ?: "",
        input.date ?: "",
        input.address ?: "",
        input.imageUrl ?: ""
    )
}