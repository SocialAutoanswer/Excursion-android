package ru.exursion.data.locations

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.AudioLocationDto
import ru.exursion.data.models.Event
import ru.exursion.data.models.EventDto
import ru.exursion.data.models.Hotel
import ru.exursion.data.models.HotelDto
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.Message
import ru.exursion.data.models.MessageDto
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDto
import ru.exursion.data.network.ExcursionApi
import ru.exursion.data.network.createHttpError
import javax.inject.Inject

interface FavoritesRepository {
    fun getFavoriteLocations(): Single<Result<List<AudioLocation>>>
    fun getFavoriteRoutes(): Single<Result<List<Route>>>
    fun getFavoriteHotels(): Single<Result<List<Hotel>>>
    fun getFavoriteEvents(): Single<Result<List<Event>>>
    fun clearAllFavoriteLocations(): Single<Result<Unit>>
    fun clearAllFavoriteRoutes(): Single<Result<Unit>>
    fun clearAllFavoriteHotels(): Single<Result<Unit>>
    fun clearAllFavoriteEvents(): Single<Result<Unit>>
    fun changeRouteFavoriteState(routeId: Long): Single<Result<Message>>
    fun changeLocationFavoriteState(locationId: Long): Single<Result<Message>>
    fun changeHotelFavoriteState(hotelId: Long): Single<Result<Message>>
    fun changeEventFavoriteState(eventId: Long): Single<Result<Message>>
}

class FavoritesRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val locationMapper: Mapper<LocationDto, Location>,
    private val audioLocationMapper: Mapper<AudioLocationDto, AudioLocation>,
    private val routesMapper: Mapper<RouteDto, Route>,
    private val hotelsMapper: Mapper<HotelDto, Hotel>,
    private val eventMapper: Mapper<EventDto, Event>,
    private val messageMapper: Mapper<MessageDto, Message>
) : FavoritesRepository {

    private fun <I : Any, O : Any> getData(
        method: Single<Response<List<I>>>,
        mapper: Mapper<I, O>
    ): Single<Result<List<O>>> {
        return method
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(mapper.mapList(dto))
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }

    private fun sendClearRequest(
        method: Single<Response<Unit>>
    ): Single<Result<Unit>> {
        return method
            .subscribeOn(Schedulers.io())
            .map {
                if(it.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }

    private fun changeState(
        method: Single<Response<MessageDto>>
    ): Single<Result<Message>> {
        return method
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(messageMapper.map(dto))
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }


    override fun getFavoriteLocations(): Single<Result<List<AudioLocation>>> {
        return getData(
            api.getFavoriteLocations(),
            audioLocationMapper
        )
    }

    override fun getFavoriteRoutes(): Single<Result<List<Route>>> {
        return getData(
            api.getFavoriteRoutes(),
            routesMapper
        )
    }

    override fun getFavoriteHotels(): Single<Result<List<Hotel>>> {
        return getData(
            api.getFavoriteHotels(),
            hotelsMapper
        )
    }

    override fun getFavoriteEvents(): Single<Result<List<Event>>> {
        return getData(
            api.getFavoriteEvents(),
            eventMapper
        )
    }

    override fun clearAllFavoriteLocations(): Single<Result<Unit>> {
        return sendClearRequest(api.clearAllFavoriteLocations())
    }

    override fun clearAllFavoriteRoutes(): Single<Result<Unit>> {
        return sendClearRequest(api.clearAllFavoriteRoutes())
    }

    override fun clearAllFavoriteHotels(): Single<Result<Unit>> {
        return sendClearRequest(api.clearAllFavoriteHotels())
    }

    override fun clearAllFavoriteEvents(): Single<Result<Unit>> {
        return sendClearRequest(api.clearAllFavoriteEvents())
    }

    override fun changeRouteFavoriteState(routeId: Long): Single<Result<Message>> {
        return changeState(api.changeRouteFavoriteState(routeId))
    }

    override fun changeLocationFavoriteState(locationId: Long): Single<Result<Message>> {
        return changeState(api.changeLocationFavoriteState(locationId))
    }

    override fun changeHotelFavoriteState(hotelId: Long): Single<Result<Message>> {
        return changeState(api.changeHotelFavoriteState(hotelId))
    }

    override fun changeEventFavoriteState(eventId: Long): Single<Result<Message>> {
        return changeState(api.changeEventFavoriteState(eventId))
    }
}