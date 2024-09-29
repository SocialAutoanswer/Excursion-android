package ru.exursion.domain

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.models.Shop
import ru.exursion.data.models.Tag
import ru.exursion.data.recommendations.RecommendationsRepository
import javax.inject.Inject

interface RecommendationsUseCase {
    fun getRecommendationsTags(city: String): Single<List<Tag>>
    fun getShops(tagId: Long): Single<List<Shop>>
}

class RecommendationsUseCaseImpl @Inject constructor(
    private val recommendationsRepository: RecommendationsRepository
) : RecommendationsUseCase {

    override fun getRecommendationsTags(city: String): Single<List<Tag>> =
        recommendationsRepository.getRecommendationTags(city)
            .map { result ->
                result.getOrThrow()
            }
            .map { it.distinct() }
            .observeOn(AndroidSchedulers.mainThread())

    override fun getShops(tagId: Long): Single<List<Shop>> {
        return recommendationsRepository.getShops(tagId)
            .map { it.getOrThrow() }
            .observeOn(AndroidSchedulers.mainThread())
    }
}