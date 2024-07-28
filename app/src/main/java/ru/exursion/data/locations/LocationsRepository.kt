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
import ru.exursion.data.locations.paging.TagsPagingSource
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.AudioLocationDto
import ru.exursion.data.models.City
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.TagItem
import ru.exursion.data.network.ExcursionApi
import ru.exursion.data.network.createHttpError
import javax.inject.Inject

interface LocationsRepository {

    fun getCities(): Flowable<PagingData<City>>
    fun getTags(): Flowable<PagingData<TagItem>>
    fun getRoutesByTag(tagId: Long): Single<Result<List<Route>>>
    fun getLocationsByCity(cityId: Long): Single<Result<List<Location>>>
    fun getLocationById(locationId: Long): Single<Result<AudioLocation>>
}

class LocationsRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val citiesPagingSource: CitiesPagingSource,
    private val tagsPagingSource: TagsPagingSource,
    private val locationMapper: Mapper<LocationDto, Location>,
    private val audioLocationMapper: Mapper<AudioLocationDto, AudioLocation>,
    private val tagsMapper: Mapper<TagDto, Tag>
) : LocationsRepository {

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }

    override fun getCities(): Flowable<PagingData<City>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) { citiesPagingSource }
    }

    override fun getTags(): Flowable<PagingData<TagItem>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) { tagsPagingSource }
    }

    override fun getRoutesByTag(tagId: Long): Single<Result<List<Route>>> {
        return api.requestRoutesByTag(tagId)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(tagsMapper.map(dto).routes)
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }

    override fun getLocationsByCity(cityId: Long): Single<Result<List<Location>>> {
        return api.requestLocationsByCity(cityId, 1)
            .subscribeOn(Schedulers.io())
            .map {
                if(it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    
                    val data = dto.data?.let { locations ->
                        locationMapper.mapList(locations.filterNotNull())
                    } ?: emptyList()
                    
                    Result.success(data)
                    
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

}