package ru.exursion.domain

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.locations.EventRepository
import ru.exursion.data.models.Event
import javax.inject.Inject

interface EventsUseCase {
    fun getEventById(eventId: Long): Single<Event>
}

class EventsUseCaseImpl @Inject constructor(
    private val eventsRepository: EventRepository
): EventsUseCase {

    override fun getEventById(eventId: Long): Single<Event> {
        return eventsRepository.getEventById(eventId)
            .map { result ->
                result.getOrThrow()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
}