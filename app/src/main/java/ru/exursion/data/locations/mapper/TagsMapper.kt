package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.TagType
import javax.inject.Inject

class TagsMapper @Inject constructor(): Mapper<TagDto, Tag> {

    override fun map(input: TagDto) = Tag(
        id = input.id ?: -1,
        name = input.name ?: "",
        iconName = input.picture?.let { "ss_ic_$it" } ?: "ss_ic_all",
        tagType = TagType.ROUTES
    )
}