package ru.exursion.data.routes

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.bibaboba.kit.util.createPagingDataFlowable
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.InternalServerException
import ru.exursion.data.models.Message
import ru.exursion.data.models.MessageDto
import ru.exursion.data.routes.paging.RouteTagsPagingSource
import ru.exursion.data.routes.paging.RoutesPagingSource
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDetails
import ru.exursion.data.models.RouteDetailsDto
import ru.exursion.data.models.Tag
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

interface RoutesRepository {
    fun getRoutes(cityId: Long? = null, tagId: Long? = null): Flowable<PagingData<Route>>
    fun getRoutesTags(): Flowable<PagingData<Tag>>
    fun getRouteById(routeId: Long): Single<Result<RouteDetails>>
    fun changeRouteFavoriteState(routeId: Long): Single<Result<Message>>
}

class RoutesRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val routesPagingSourceFactory: RoutesPagingSource.Factory,
    private val routeTagsPagingSourceFactory: RouteTagsPagingSource.Factory,
    private val routeMapper: Mapper<RouteDetailsDto, RouteDetails>,
    private val messageMapper: Mapper<MessageDto, Message>
) : RoutesRepository {

    companion object {
        const val DEFAULT_PAGE_SIZE = 100
    }

    override fun getRoutes(cityId: Long?, tagId: Long?): Flowable<PagingData<Route>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) {
            routesPagingSourceFactory.create(
                cityId = cityId,
                tagId = tagId
            )
        }
    }

    override fun getRoutesTags(): Flowable<PagingData<Tag>> {
        return createPagingDataFlowable(DEFAULT_PAGE_SIZE) { routeTagsPagingSourceFactory.create() }
    }

    override fun getRouteById(routeId: Long): Single<Result<RouteDetails>> {
        return api.getRouteById(routeId)
            .subscribeOn(Schedulers.io())
            .map {
                if(it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())
                    Result.success(routeMapper.map(dto))
                } else {
                    when(it.code()) {
                        500 -> Result.failure(InternalServerException())
                        else -> Result.failure(CanNotGetDataException())
                    }
                }
            }
    }

    override fun changeRouteFavoriteState(routeId: Long): Single<Result<Message>> {
        return api.changeRouteFavoriteState(routeId)
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