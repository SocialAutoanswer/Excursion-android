package ru.exursion.domain

import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.Location
import ru.exursion.data.models.Message
import javax.inject.Inject

interface LocationUseCase {

    fun getLocationsByCity(cityId: Long): Single<List<Location>>
    fun getLocationById(locationId: Long): Single<AudioLocation>
    fun changeLocationFavoriteState(locationId: Long): Single<Message>
    fun getCityBoundingBox(points: List<Point>): BoundingBox
}

class LocationUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository
): LocationUseCase {

    override fun getLocationsByCity(cityId: Long): Single<List<Location>> {
        return locationsRepository.getLocationsByCity(cityId)
            .map { result ->
                if(result.isFailure) {
                    throw result.exceptionOrNull() ?: CanNotGetDataException()
                }
                result.getOrNull() ?: emptyList()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getLocationById(locationId: Long): Single<AudioLocation> {
        return locationsRepository.getLocationById(locationId)
            .map { result ->
                if(result.isFailure) {
                    throw result.exceptionOrNull() ?: CanNotGetDataException()
                }
                result.getOrNull() ?: AudioLocation(-1, "", "", false, null, emptyList(), emptyList())
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun changeLocationFavoriteState(locationId: Long): Single<Message> {
        return locationsRepository.changeLocationFavoriteState(locationId)
            .map { result ->
                if (result.isFailure) {
                    throw result.exceptionOrNull() ?: CanNotGetDataException()
                }

                result.getOrNull() ?: Message("")
            }
    }

    override fun getCityBoundingBox(points: List<Point>): BoundingBox {
        if (points.isEmpty()) {
            return BoundingBox(
                Point(MIN_LATITUDE, MIN_LONGITUDE),
                Point(MAX_LATITUDE, MAX_LONGITUDE)
            )
        }

        var minLatitude = MAX_LATITUDE
        var maxLatitude = MIN_LATITUDE

        var maxLongitude = MIN_LONGITUDE
        var minLongitude = MAX_LONGITUDE

        points.forEach { point ->
            if(point.latitude < minLatitude) minLatitude = point.latitude
            if(point.latitude > maxLatitude) maxLatitude = point.latitude

            if(point.longitude < minLongitude) minLongitude = point.longitude
            if(point.longitude > maxLongitude) maxLongitude = point.longitude
        }

        return BoundingBox(
            Point(minLatitude, minLongitude),
            Point(maxLatitude, maxLongitude)
        )
    }


    companion object {
        const val MAX_LATITUDE = 89.3
        const val MIN_LATITUDE = -89.3
        const val MAX_LONGITUDE = 179.99
        const val MIN_LONGITUDE = -180.0

        //coordinates from documentation https://yandex.ru/dev/mapkit/doc/ru/com/yandex/mapkit/map/CameraBounds
    }
}