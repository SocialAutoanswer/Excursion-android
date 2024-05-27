package ru.exursion.data.locations

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import io.reactivex.rxjava3.core.Flowable
import ru.exursion.data.models.City
import ru.exursion.data.models.Tag
import javax.inject.Inject

interface LocationsRepository {

    fun getCities(): Flowable<PagingData<City>>

    fun getTags(): Flowable<PagingData<Tag>>
}

class LocationsRepositoryImpl @Inject constructor(
    private val citiesPagingSource: CitiesPagingSource,
    private val tagsPagingSource: TagsPagingSource,
) : LocationsRepository {

    override fun getCities(): Flowable<PagingData<City>> {
        return Pager(
            PagingConfig(pageSize = 100),
            pagingSourceFactory = { citiesPagingSource }
        ).flowable
    }

    override fun getTags(): Flowable<PagingData<Tag>> {
        return Pager(
            PagingConfig(pageSize = 100),
            pagingSourceFactory = { tagsPagingSource }
        ).flowable
    }
}