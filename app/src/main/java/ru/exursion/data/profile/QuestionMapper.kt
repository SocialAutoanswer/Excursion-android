package ru.exursion.data.profile

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Question
import ru.exursion.data.models.QuestionDto
import javax.inject.Inject

class QuestionMapper @Inject constructor(): Mapper<QuestionDto, Question> {

    override fun map(input: QuestionDto) = Question(
        input.id ?: -1,
        input.question ?: "",
        input.answer ?: ""
    )

}