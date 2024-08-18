package ru.exursion.data.locations

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.models.Event
import ru.exursion.data.models.EventDto
import ru.exursion.data.network.ExcursionApi
import ru.exursion.data.network.createHttpError
import javax.inject.Inject

interface EventRepository {
    fun getEventById(eventId: Long): Single<Result<Event>>
}

class EventRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val eventMapper: Mapper<EventDto, Event>
): EventRepository {

    override fun getEventById(eventId: Long): Single<Result<Event>> {
        return api.getEventById(eventId)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(eventMapper.map(dto))
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }
}