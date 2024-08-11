package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class RouteDetailsDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("length") val kilometers: Double?,
    @SerializedName("duration") val durationInMinutes: Int?,
    @SerializedName("tags") val tags: List<TagDto?>?,
    @SerializedName("locations") val locations: List<LocationDto?>?,
    @SerializedName("isFavorite") val isFavorite: Boolean?,
    @SerializedName("reviews") val reviews: List<ReviewDto?>?,
    @SerializedName("locations_length") val locationsLength: Int?
)

data class RouteDetails(
    val id: Long,
    val name: String,
    val description: String,
    val kilometers: Double,
    val durationInMinutes: Int,
    val tags: List<Tag>,
    val locations: List<Location>,
    val isFavorite: Boolean,
    val reviews: List<Review>,
    val isPaid: Boolean,
    val image: String
)