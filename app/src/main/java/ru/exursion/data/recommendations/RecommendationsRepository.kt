package ru.exursion.data.recommendations

import io.reactivex.rxjava3.core.Single
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.TagType
import ru.exursion.data.network.ExcursionApi
import ru.exursion.data.network.createHttpError
import javax.inject.Inject

interface RecommendationsRepository {
    fun getRecommendationTags(): Single<Result<List<Tag>>>
}

class RecommendationsRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val tagsMapper: Mapper<TagDto, Tag>
): RecommendationsRepository {

    override fun getRecommendationTags(): Single<Result<List<Tag>>> {
        return api.getRecommendationsTags()
            .map {
                if (it.isSuccessful) {
                    val tags = arrayListOf<Tag>()
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())

                    tags.addAll(
                        tagsMapper.mapList(dto.routeTags?.filterNotNull() ?: emptyList()).map { it.also{ it.tagType = TagType.ROUTES } } +
                        tagsMapper.mapList(dto.eventTags?.filterNotNull() ?: emptyList()).map { it.also{ it.tagType = TagType.EVENTS } } +
                        tagsMapper.mapList(dto.locationTags?.filterNotNull() ?: emptyList()).map { it.also{ it.tagType = TagType.LOCATIONS } }
                    )

                    Result.success(tags)
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }

}