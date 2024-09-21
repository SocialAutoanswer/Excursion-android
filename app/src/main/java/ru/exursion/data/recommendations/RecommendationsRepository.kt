package ru.exursion.data.recommendations

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.models.Shop
import ru.exursion.data.models.ShopDto
import ru.exursion.data.models.Tag
import ru.exursion.data.models.TagDto
import ru.exursion.data.models.TagType
import ru.exursion.data.network.ExcursionApi
import ru.exursion.data.network.createHttpError
import javax.inject.Inject

interface RecommendationsRepository {
    fun getRecommendationTags(city: String): Single<Result<List<Tag>>>
    fun getShops(tagId: Long): Single<Result<List<Shop>>>
}

class RecommendationsRepositoryImpl @Inject constructor(
    private val api: ExcursionApi,
    private val tagsMapper: Mapper<TagDto, Tag>,
    private val shopMapper: Mapper<ShopDto, Shop>
): RecommendationsRepository {

    override fun getRecommendationTags(city: String): Single<Result<List<Tag>>> {
        return api.getRecommendationsTags(city)
            .map {
                if (it.isSuccessful) {
                    val tags = arrayListOf<Tag>()
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())

                    tags.addAll(
                        tagsMapper.mapList(dto.routeTags?.filterNotNull() ?: emptyList()).map { it.also{ it.tagType = TagType.ROUTES } } +
                        tagsMapper.mapList(dto.eventTags?.filterNotNull() ?: emptyList()).map { it.also{ it.tagType = TagType.EVENTS } } +
                        tagsMapper.mapList(dto.locationTags?.filterNotNull() ?: emptyList()).map { it.also{ it.tagType = TagType.LOCATIONS } } +
                        tagsMapper.mapList(dto.shopTags?.filterNotNull() ?: emptyList()).map { it.also { it.tagType = TagType.SHOP } }
                    )

                    Result.success(tags)
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }

    override fun getShops(tagId: Long): Single<Result<List<Shop>>> {
        return api.getShopsAndFood(tagId)
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    val dto = it.body() ?: return@map Result.failure(CanNotGetDataException())

                    Result.success(shopMapper.mapList(dto))
                } else {
                    Result.failure(createHttpError(it))
                }
            }
    }
}