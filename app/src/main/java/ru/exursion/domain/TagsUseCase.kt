package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.Tag
import javax.inject.Inject

interface TagsUseCase {

    fun getTags(): Flowable<PagingData<Tag>>
}

class TagsUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository,
) : TagsUseCase {

    override fun getTags(): Flowable<PagingData<Tag>> {
        return locationsRepository.getTags()
    }

}