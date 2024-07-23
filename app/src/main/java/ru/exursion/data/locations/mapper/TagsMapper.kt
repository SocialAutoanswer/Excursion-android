package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import javax.inject.Inject

class TagsMapper @Inject constructor(): Mapper<TagDto, Tag> {

    override fun map(input: TagDto) = Tag(
        id = input.id ?: -1,
        name = input.name ?: "",
        iconName = input.picture?.let { "ss_ic_$it" } ?: "ic_cross",
        routesIds = input.routes?.filterNotNull() ?: emptyList(),
        tagsIds = input.tags?.filterNotNull() ?: emptyList()
    )

    override fun mapList(input: List<TagDto>): List<Tag> {
        return input.map(::map)
    }
}