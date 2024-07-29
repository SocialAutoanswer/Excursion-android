package ru.exursion.data.locations.paging

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.DefaultRxPagingSource
import ru.exursion.data.models.TagItem
import ru.exursion.data.models.TagItemDto
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

class TagsPagingSource @Inject constructor(
    private val api: ExcursionApi,
    private val tagsMapper: Mapper<TagItemDto, TagItem>
): DefaultRxPagingSource<TagItem>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, TagItem>> {
        val pageNumber = params.key ?: 1

        return api.requestTags(pageNumber)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.isSuccessful) {

                    val pageDto = response.body()

                    val data = pageDto?.tags?.let {
                        tagsMapper.mapList(it.filterNotNull())
                    } ?: emptyList()

                    val nextPageNumber = if (pageDto?.nextPage == null) null else pageNumber.inc()
                    val previousPageNumber = if (pageDto?.previousPage == null) null else pageNumber.dec()

                    LoadResult.Page(data, previousPageNumber, nextPageNumber)
                } else {
                    handleHttpErrors(response)
                }
            }
    }
}