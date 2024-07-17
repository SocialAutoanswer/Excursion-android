package ru.bibaboba.kit.util

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import androidx.paging.PagingSource
import androidx.paging.rxjava3.flowable

fun <K: Any, V: Any> createPagingDataFlowable(
    pageSize: Int,
    pagingSourceFactory: () -> PagingSource<K, V>
): Flowable<PagingData<V>> {
    return Pager(
        PagingConfig(pageSize = pageSize),
        pagingSourceFactory = pagingSourceFactory
    ).flowable
}