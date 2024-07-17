package ru.bibaboba.kit.paging

import android.util.Log
import androidx.paging.PagingSource
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.rx3.await

/**
 * RxPagingSource that fixes error handling in original RxPagingSource
 *
 * @see: https://androidx.tech/artifacts/paging/paging-rxjava2/3.1.1-source/androidx/paging/rxjava2/RxPagingSource.kt.html
 */
abstract class BibaRxPagingSource<Key : Any, Value : Any> : PagingSource<Key, Value>() {
    /**
     * Loading API for [PagingSource].
     *
     * Implement this method to trigger your async load (e.g. from database or network).
     */
    abstract fun loadSingle(params: LoadParams<Key>): Single<LoadResult<Key, Value>>

    final override suspend fun load(params: LoadParams<Key>): LoadResult<Key, Value> {
        try {
            return loadSingle(params).await()
        } catch (t: Throwable) {
            Log.e(this::class.simpleName, "Error while loading: ${t::class.simpleName}", t)
            return LoadResult.Error(t)
        }
    }
}