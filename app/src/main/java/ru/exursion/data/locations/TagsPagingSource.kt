package ru.exursion.data.locations

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.DefaultRxPagingSource
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

class TagsPagingSource @Inject constructor(
    private val api: ExcursionApi,
    private val tagsMapper: Mapper<TagDto, Tag>
): DefaultRxPagingSource<Tag>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Tag>> {
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
                    LoadResult.Error(CanNotGetDataException())
                }
            }
    }
}