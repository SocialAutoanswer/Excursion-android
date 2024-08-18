package ru.exursion.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ReviewDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("review") val reviewText: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("raiting") val rating: Int?,
    @SerializedName("user_name") val userName: String?
)

data class Review(
    val id: Long,
    val reviewText: String,
    val createdAt: LocalDateTime,
    val rating: Int,
    val userName: String
)