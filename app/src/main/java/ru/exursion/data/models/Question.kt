package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class QuestionDto (
    @SerializedName("") val question: String?,
    @SerializedName("") val answer: String?
)

data class Question (
    val question: String,
    val answer: String
)