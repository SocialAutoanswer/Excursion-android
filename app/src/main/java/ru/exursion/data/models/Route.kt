package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class RouteRequestDto(
    @SerializedName("next") val nextPage: String?,
    @SerializedName("previous") val previousPage: String?,
    @SerializedName("data") val routes: List<RouteDto?>?,
)

data class RouteDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("length") val kilometers: Double?,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("city") val city: Long?,
    @SerializedName("tags") val tagIds: List<Int?>?
)

data class Route(
    val id: Long,
    val name: String,
    val description: String,
    val kilometers: Double,
    val durationInMinutes: Int,
    val tags: List<Int>,
    val isPaid: Boolean,
    val image: String
)