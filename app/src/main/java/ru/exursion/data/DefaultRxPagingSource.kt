package ru.exursion.data

import androidx.paging.PagingState
import retrofit2.Response
import ru.bibaboba.kit.paging.BibaRxPagingSource
import ru.exursion.data.network.createHttpError

abstract class DefaultRxPagingSource<T: Any> : BibaRxPagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }

    protected fun <D> handleHttpErrors(response: Response<D>): LoadResult.Error<Int, T> {
        return LoadResult.Error(createHttpError(response))
    }
}