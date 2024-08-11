package ru.exursion.data.locations.paging

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.ExcursionPagingSource
import ru.exursion.data.models.Location
import ru.exursion.data.models.LocationDto
import ru.exursion.data.models.PageDto
import ru.exursion.data.network.ExcursionApi

class LocationsPagingSource @AssistedInject constructor(
    private val api: ExcursionApi,
    locationsMapper: Mapper<LocationDto, Location>,
    @Assisted("cityId") cityId: Long? = null,
    @Assisted("tagId") tagId: Long? = null,
    @Assisted("routeId") private val routeId: Long? = null
) : ExcursionPagingSource<LocationDto, Location>(
    mapper = locationsMapper,
    cityId = cityId,
    tagId = tagId
) {

    override fun getDataByTag(tagId: Long, pageNumber: Int) = api.getLocationsByTag(tagId, pageNumber)

    override fun getDataByCity(cityId: Long, pageNumber: Int) = api.getLocationsByCity(cityId, pageNumber)

    override fun getData(pageNumber: Int) = api.getLocations(pageNumber)

    override fun getMethod(pageNumber: Int): Single<Response<PageDto<LocationDto>>> {
        if (routeId != null) {
            return api.getLocationsByRoute(routeId, pageNumber)
        }

        return super.getMethod(pageNumber)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("cityId") cityId: Long? = null,
            @Assisted("tagId") tagId: Long? = null,
            @Assisted("routeId") routeId: Long? = null
        ): LocationsPagingSource
    }
}