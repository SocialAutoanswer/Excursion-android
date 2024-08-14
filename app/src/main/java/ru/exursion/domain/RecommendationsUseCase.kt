package ru.exursion.domain

import android.util.Log
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import ru.exursion.data.models.Tag
import ru.exursion.data.recommendations.RecommendationsRepository
import javax.inject.Inject

interface RecommendationsUseCase {
    fun getRecommendationsTags(): Single<List<Tag>>
}

class RecommendationsUseCaseImpl @Inject constructor(
    private val recommendationsRepository: RecommendationsRepository
) : RecommendationsUseCase {

    override fun getRecommendationsTags(): Single<List<Tag>> =
        recommendationsRepository.getRecommendationTags()
            .map { result ->
                result.getOrThrow()
            }
            .observeOn(AndroidSchedulers.mainThread())
}