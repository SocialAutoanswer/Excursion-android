package ru.exursion.data.routes.paging

import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.DefaultRxPagingSource
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.network.ExcursionApi

class RouteTagsPagingSource @AssistedInject constructor(
    private val api: ExcursionApi,
    private val tagsMapper: Mapper<TagDto, Tag>
) : DefaultRxPagingSource<Tag>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Tag>> {
        val pageNumber = params.key ?: 1

        return api.getRoutesTags(pageNumber)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.isSuccessful) {

                    val pageDto = response.body()

                    val data = pageDto?.data?.let {
                        tagsMapper.mapList(it.filterNotNull())
                    } ?: emptyList()

                    val nextPageNumber = if (pageDto?.nextPageLink == null) null else pageNumber.inc()
                    val previousPageNumber = if (pageDto?.previousPageLink == null) null else pageNumber.dec()

                    LoadResult.Page(data, previousPageNumber, nextPageNumber)
                } else {
                    handleHttpErrors(response)
                }
            }
    }

    @AssistedFactory
    interface Factory {
        fun create(): RouteTagsPagingSource
    }
}