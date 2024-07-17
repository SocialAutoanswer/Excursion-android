package ru.exursion.data.profile

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.CanNotGetDataException
import ru.exursion.data.DefaultRxPagingSource
import ru.exursion.data.models.Question
import ru.exursion.data.models.QuestionDto
import ru.exursion.data.network.ExcursionApi
import javax.inject.Inject

class QuestionPagingSource @Inject constructor(
    private val api: ExcursionApi,
    private val questionMapper: Mapper<QuestionDto, Question>
): DefaultRxPagingSource<Question>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Question>> {
        val pageNumber = params.key ?: 1

        return api.getQuestions(pageNumber)
            .subscribeOn(Schedulers.io())
            .map { response ->
                if (response.isSuccessful) {

                    val pageDto = response.body()

                    val data = pageDto?.data?.let {
                        questionMapper.mapList(it.filterNotNull())
                    } ?: emptyList()

                    val nextPageNumber = if (pageDto?.nextPage == null) null else pageNumber.inc()
                    val previousPageNumber = if (pageDto?.previousPage == null) null else pageNumber.dec()

                    LoadResult.Page(data, previousPageNumber, nextPageNumber)
                } else {
                    LoadResult.Error(CanNotGetDataException())
                }
            }
    }
}