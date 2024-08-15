package ru.exursion.data

import androidx.annotation.CallSuper
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Response
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.PageDto
import ru.exursion.ui.shared.ext.isValidId

abstract class ExcursionPagingSource<I: Any, O: Any>(
    private val mapper: Mapper<I, O>,
    private val cityId: Long?,
    private val tagId: Long?
): DefaultRxPagingSource<O>() {

    abstract fun getDataByTag(tagId: Long, pageNumber: Int): Single<Response<PageDto<I>>>
    abstract fun getDataByCity(cityId: Long, pageNumber: Int): Single<Response<PageDto<I>>>
    abstract fun getData(pageNumber: Int): Single<Response<PageDto<I>>>

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, O>> {
        val pageNumber = params.key ?: 1

        return getMethod(pageNumber)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.isSuccessful) {

                    val pageDto = response.body()
                    val data = map(pageDto)

                    val nextPageNumber = if (pageDto?.nextPageLink == null) null else pageNumber.inc()
                    val previousPageNumber = if (pageDto?.previousPageLink == null) null else pageNumber.dec()

                    LoadResult.Page(data, previousPageNumber, nextPageNumber)
                } else {
                    handleHttpErrors(response)
                }
            }
    }

    @CallSuper
    protected open fun getMethod(pageNumber: Int): Single<Response<PageDto<I>>> {
        if (tagId.isValidId()) {
            return getDataByTag(tagId!!, pageNumber)
        }
        if (cityId.isValidId()) {
            return getDataByCity(cityId!!, pageNumber)
        }
        // you can add new conditions below, but dont touch upper ones

        return getData(pageNumber)
    }

    @CallSuper
    protected open fun map(pageDto: PageDto<I>?): List<O> {
        return pageDto?.let {
            mapper.mapList(it.data?.filterNotNull() ?: emptyList())
        } ?: emptyList()
    }
}