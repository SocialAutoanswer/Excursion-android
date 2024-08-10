package ru.exursion.data.locations

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.bibaboba.kit.util.createPagingDataFlowable
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.InternalServerException
import ru.exursion.data.locations.paging.CitiesPagingSource
import ru.exursion.data.locations.paging.LocationsPagingSource
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.AudioLocationDto
import ru.exursion.data.models.City
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.Message
import ru.exursion.data.models.MessageDto
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

interface LocationsRepository {
    fun getCities(): Flowable<PagingData<City>>
    fun getLocations(cityId: Long? = null, tagId: Long? = null, routeId: Long? = null): Flowable<PagingData<Location>>
    fun getLocations(cityId: Long): Single<Result<List<Location>>>
    fun getLocationById(locationId: Long): Single<Result<AudioLocation>>
    fun changeLocationFavoriteState(locationId: Long): Single<Result<Message>>
}

class LocationsRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val citiesPagingSourceFactory: CitiesPagingSource.Factory,
    private val locationsPagingSourceFactory: LocationsPagingSource.Factory,
    private val locationsMapper: Mapper<LocationDto, Location>,
    private val audioLocationMapper: Mapper<AudioLocationDto, AudioLocation>,
    private val messageMapper: Mapper<MessageDto, Message>
) : LocationsRepository {

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }

    override fun getCities(): Flowable<PagingData<City>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) { citiesPagingSourceFactory.create() }
    }

    override fun getLocations(cityId: Long?, tagId: Long?, routeId: Long?): Flowable<PagingData<Location>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) {
            locationsPagingSourceFactory.create(
                cityId = cityId,
                tagId = tagId,
                routeId = routeId
            )
        }
    }

    override fun getLocations(cityId: Long): Single<Result<List<Location>>> { //a hack because only paging data comes from server
        return api.getLocationsByCity(cityId,1)
            .subscribeOn(Schedulers.io())
            .map {
                if(it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(locationsMapper.mapList(dto.data?.filterNotNull() ?: emptyList()))
                } else {
                    when(it.code()) {
                        500 -> Result.failure(InternalServerException())
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
    }

    override fun getLocationById(locationId: Long): Single<Result<AudioLocation>> {
        return api.getLocationById(locationId)
            .subscribeOn(Schedulers.io())
            .map {
                if(it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(audioLocationMapper.map(dto))
                } else {
                    when(it.code()) {
                        500 -> Result.failure(InternalServerException())
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
    }

    override fun changeLocationFavoriteState(locationId: Long): Single<Result<Message>> {
        return api.changeLocationFavoriteState(locationId)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(messageMapper.map(dto))
                } else {
                    when(it.code()) {
                        500 -> Result.failure(InternalServerException())
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
    }
}