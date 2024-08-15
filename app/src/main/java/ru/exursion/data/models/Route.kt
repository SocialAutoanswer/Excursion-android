package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class RouteDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("image") val imageUrl: String?,
    @SerializedName("length") val kilometers: Double?,
    @SerializedName("duration") val duration: Int?,
    @SerializedName("price") val price: Int?,
    @SerializedName("city") val cityId: Long?,
    //TODO: @SerializedName("tags") val tagIds: List<Long?>?
    @SerializedName("locations") val locations: List<LocationDto?>?
)

data class Route(
    val id: Long,
    val name: String,
    val description: String,
    val imageUrl: String,
    val kilometers: Double,
    val durationInMinutes: Int,
    val tags: List<Long>,
    val price: Int,
    val cityId: Long,
    val isPaid: Boolean
)