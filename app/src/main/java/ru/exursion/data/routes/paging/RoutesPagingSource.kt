package ru.exursion.data.routes.paging

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.ExcursionPagingSource
import ru.exursion.data.models.PageDto
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDto
import ru.exursion.data.network.ExcursionApi
import ru.exursion.ui.shared.ext.isValidId

class RoutesPagingSource @AssistedInject constructor(
    private val api: ExcursionApi,
    routesMapper: Mapper<RouteDto, Route>,
    @Assisted("cityId") private val cityId: Long? = null,
    @Assisted("tagId") private val tagId: Long? = null
) : ExcursionPagingSource<RouteDto, Route>(
    mapper = routesMapper,
    cityId = cityId,
    tagId = tagId
) {

    override fun getDataByTag(tagId: Long, pageNumber: Int) = api.getRoutesByTag(tagId, pageNumber)

    override fun getDataByCity(cityId: Long, pageNumber: Int) = api.getRoutesByCity(cityId, pageNumber)

    override fun getData(pageNumber: Int) = api.getRoutes(pageNumber)

    override fun map(pageDto: PageDto<RouteDto>?): List<Route> {
        if (cityId.isValidId() && tagId.isValidId()) {
            // hack because server does not have a method with two filters
            return super.map(pageDto).filter { it.cityId == cityId }
        }
        return super.map(pageDto)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("cityId") cityId: Long? = null,
            @Assisted("tagId") tagId: Long? = null
        ): RoutesPagingSource
    }
}