package ru.exursion.data.locations

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import javax.inject.Inject

class TagsMapper @Inject constructor(): Mapper<TagDto, Tag> {

    override fun map(input: TagDto) = Tag(
        id = input.id ?: -1,
        name = input.name ?: "",
        iconName = input.picture ?: "ic_cross",
        routesIds = input.routes?.filterNotNull() ?: emptyList(),
        tagsIds = input.tags?.filterNotNull() ?: emptyList()
    )

}