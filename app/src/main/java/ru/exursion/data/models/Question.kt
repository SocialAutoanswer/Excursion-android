package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class PageDto<T> (
    @SerializedName("next") val nextPage: String?,
    @SerializedName("previous") val previousPage: String?,
    @SerializedName("data") val data: List<T?>?
)

data class QuestionDto (
    @SerializedName("question") val question: String?,
    @SerializedName("answer") val answer: String?
)

data class Question (
    val question: String,
    val answer: String
)