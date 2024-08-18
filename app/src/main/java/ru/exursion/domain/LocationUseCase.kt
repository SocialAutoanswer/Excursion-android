package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.Location
import javax.inject.Inject

interface LocationUseCase {
    fun getLocations(cityId: Long? = null, tagId: Long? = null, routeId: Long? = null): Flowable<PagingData<Location>>
    fun getLocations(cityId: Long): Single<List<Location>>
    fun getLocationById(locationId: Long): Single<AudioLocation>
}

class LocationUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository
): LocationUseCase {

    override fun getLocations(cityId: Long?, tagId: Long?, routeId: Long?): Flowable<PagingData<Location>> {
        return locationsRepository.getLocations(
            cityId = cityId,
            tagId = tagId,
            routeId = routeId
        )
    }

    override fun getLocations(cityId: Long): Single<List<Location>> {
        return locationsRepository.getLocations(cityId)
            .map { result ->
                result.getOrThrow()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getLocationById(locationId: Long): Single<AudioLocation> {
        return locationsRepository.getLocationById(locationId)
            .map { result ->
                result.getOrThrow()
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

}