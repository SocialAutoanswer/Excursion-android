package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class QuestionDto (
    @SerializedName("id") val id: Int?,
    @SerializedName("question") val question: String?,
    @SerializedName("answer") val answer: String?
)

data class Question (
    val id: Int,
    val question: String,
    val answer: String
)