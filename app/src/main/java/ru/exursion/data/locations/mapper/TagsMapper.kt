package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import javax.inject.Inject

class TagsMapper @Inject constructor(): Mapper<TagDto, Tag> {

    override fun map(input: TagDto) = Tag(
        id = input.id ?: -1,
        name = input.name ?: "",
        iconName = input.picture?.let { "ss_ic_$it" } ?: "ss_ic_all",
        routes = input.routes?.filterNotNull() ?: emptyList()
    )
}