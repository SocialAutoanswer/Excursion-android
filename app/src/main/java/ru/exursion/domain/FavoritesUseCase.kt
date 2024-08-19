package ru.exursion.domain

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.locations.FavoritesRepository
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.Event
import ru.exursion.data.models.Hotel
import ru.exursion.data.models.Location
import ru.exursion.data.models.Message
import ru.exursion.data.models.Route
import javax.inject.Inject

interface FavoritesUseCase {
    fun getFavoriteRoutes(): Single<List<Route>>
    fun getFavoriteLocations(): Single<List<AudioLocation>>
    fun getFavoriteHotels(): Single<List<Hotel>>
    fun getFavoriteEvents(): Single<List<Event>>
    fun clearAllFavoriteLocations(): Single<Unit>
    fun clearAllFavoriteRoutes(): Single<Unit>
    fun clearAllFavoriteHotels(): Single<Unit>
    fun clearAllFavoriteEvents(): Single<Unit>
    fun changeRouteFavoriteState(routeId: Long): Single<Message>
    fun changeLocationFavoriteState(locationId: Long): Single<Message>
    fun changeHotelFavoriteState(hotelId: Long): Single<Message>
    fun changeEventFavoriteState(eventId: Long): Single<Message>

}

class FavoritesUseCaseImpl @Inject constructor(
    private val favoritesRepository: FavoritesRepository
): FavoritesUseCase {

    private fun <D: Any> getData(method: Single<Result<List<D>>>): Single<List<D>> {
        return method
            .map { result ->
                if(result.isFailure) {
                    throw result.exceptionOrNull() ?: CanNotGetDataException()
                }
                result.getOrThrow()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun sendClearRequest(method: Single<Result<Unit>>): Single<Unit> {
        return method
            .map { result ->
                if(result.isFailure) {
                    throw result.exceptionOrNull() ?: CanNotGetDataException()
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    private fun changeFavoriteState(method: Single<Result<Message>>): Single<Message> {
        return method
            .map { result ->
                if(result.isFailure) {
                    throw result.exceptionOrNull() ?: CanNotGetDataException()
                }
                result.getOrThrow()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFavoriteRoutes(): Single<List<Route>> {
        return getData(favoritesRepository.getFavoriteRoutes())
    }

    override fun getFavoriteLocations(): Single<List<AudioLocation>> {
        return getData(favoritesRepository.getFavoriteLocations())
    }

    override fun getFavoriteHotels(): Single<List<Hotel>> {
        return getData(favoritesRepository.getFavoriteHotels())
    }

    override fun getFavoriteEvents(): Single<List<Event>> {
        return getData(favoritesRepository.getFavoriteEvents())
    }

    override fun clearAllFavoriteLocations(): Single<Unit> {
        return sendClearRequest(favoritesRepository.clearAllFavoriteLocations())
    }

    override fun clearAllFavoriteRoutes(): Single<Unit> {
        return sendClearRequest(favoritesRepository.clearAllFavoriteRoutes())
    }

    override fun clearAllFavoriteHotels(): Single<Unit> {
        return sendClearRequest(favoritesRepository.clearAllFavoriteHotels())
    }

    override fun clearAllFavoriteEvents(): Single<Unit> {
        return sendClearRequest(favoritesRepository.clearAllFavoriteEvents())
    }

    override fun changeRouteFavoriteState(routeId: Long): Single<Message> {
        return changeFavoriteState(favoritesRepository.changeRouteFavoriteState(routeId))
    }

    override fun changeLocationFavoriteState(locationId: Long): Single<Message> {
        return changeFavoriteState(favoritesRepository.changeLocationFavoriteState(locationId))
    }

    override fun changeHotelFavoriteState(hotelId: Long): Single<Message> {
        return changeFavoriteState(favoritesRepository.changeHotelFavoriteState(hotelId))
    }

    override fun changeEventFavoriteState(eventId: Long): Single<Message> {
        return changeFavoriteState(favoritesRepository.changeEventFavoriteState(eventId))
    }
}