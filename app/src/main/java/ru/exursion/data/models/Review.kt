package ru.exursion.data.models

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ReviewDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("review") val reviewText: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("raiting") val rating: Int?,
    @SerializedName("user") val user: UserDto?,
    @SerializedName("route") val route: RouteDto?
)

data class Review(
    val id: Long,
    val reviewText: String,
    val createdAt: LocalDateTime,
    val rating: Int,
    val user: User?
)