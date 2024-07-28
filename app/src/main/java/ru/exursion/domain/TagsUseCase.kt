package ru.exursion.domain

import androidx.paging.PagingData
import io.reactivex.rxjava3.core.Flowable
import ru.exursion.data.locations.LocationsRepository
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagItem
import javax.inject.Inject

interface TagsUseCase {

    fun getTags(): Flowable<PagingData<TagItem>>
}

class TagsUseCaseImpl @Inject constructor(
    private val locationsRepository: LocationsRepository,
) : TagsUseCase {

    override fun getTags(): Flowable<PagingData<TagItem>> {
        return locationsRepository.getTags()
    }

}