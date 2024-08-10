package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.Tag
import ru.exursion.data.routes.RoutesRepository
import javax.inject.Inject

interface TagsUseCase {
}

class TagsUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository,
    private val routesRepository: RoutesRepository
) : TagsUseCase {


}