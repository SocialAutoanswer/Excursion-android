package ru.exursion.data

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource

abstract class DefaultRxPagingSource<T: Any> : RxPagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val anchorPage = state.closestPageToPosition(anchorPosition) ?: return null
        return anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
    }
}