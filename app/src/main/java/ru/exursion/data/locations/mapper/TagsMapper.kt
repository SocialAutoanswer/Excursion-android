package ru.exursion.data.locations.mapper

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Route
import ru.exursion.data.models.RouteDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.TagItem
import ru.exursion.data.models.TagItemDto
import javax.inject.Inject

class TagsMapper @Inject constructor(
    private val routesMapper: Mapper<RouteDto, Route>
): Mapper<TagDto, Tag> {

    override fun map(input: TagDto) = Tag(
        id = input.id ?: -1,
        name = input.name ?: "",
        iconName = input.picture?.let { "ss_ic_$it" } ?: "ss_ic_all",
        routes = routesMapper.mapList(input.routes?.filterNotNull() ?: emptyList()),
        tagsIds = input.tags?.filterNotNull() ?: emptyList()
    )

    override fun mapList(input: List<TagDto>): List<Tag> {
        return input.map(::map)
    }
}

class TagsItemMapper @Inject constructor(): Mapper<TagItemDto, TagItem> {

    override fun map(input: TagItemDto) = TagItem(
        id = input.id ?: -1,
        name = input.name ?: "",
        iconName = input.picture?.let { "ss_ic_$it" } ?: "ss_ic_all",
        routesIds = input.routes?.filterNotNull() ?: emptyList(),
        tagsIds = input.tags?.filterNotNull() ?: emptyList()
    )

    override fun mapList(input: List<TagItemDto>): List<TagItem> {
        return input.map(::map)
    }
}