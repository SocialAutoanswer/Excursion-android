package ru.exursion.domain

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.locations.FavoritesRepository
import ru.exursion.data.models.Event
import ru.exursion.data.models.Hotel
import ru.exursion.data.models.Location
import ru.exursion.data.models.Route
import javax.inject.Inject

interface FavoritesUseCase {
    fun getFavoriteRoutes(): Single<List<Route>>
    fun getFavoriteLocations(): Single<List<Location>>
    fun getFavoriteHotels(): Single<List<Hotel>>
    fun getFavoriteEvents(): Single<List<Event>>
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
                result.getOrNull() ?: emptyList()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getFavoriteRoutes(): Single<List<Route>> {
        return getData(favoritesRepository.getFavoriteRoutes())
    }

    override fun getFavoriteLocations(): Single<List<Location>> {
        return getData(favoritesRepository.getFavoriteLocations())
    }

    override fun getFavoriteHotels(): Single<List<Hotel>> {
        return getData(favoritesRepository.getFavoriteHotels())
    }

    override fun getFavoriteEvents(): Single<List<Event>> {
        return getData(favoritesRepository.getFavoriteEvents())
    }
}