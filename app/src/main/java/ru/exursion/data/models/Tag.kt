package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

enum class TagType {
    LOCATIONS, ROUTES, EVENTS
}

data class RecommendationTagsDto (
    @SerializedName("locations_tags") val locationTags: List<TagDto?>?,
    @SerializedName("routes_tags") val routeTags: List<TagDto?>?,
    @SerializedName("events_tags") val eventTags: List<TagDto?>?
)

data class TagDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("picture") val picture: String?,
    @SerializedName("type") val type: Char
)

data class Tag(
    val id: Long,
    val name: String,
    val iconName: String,
    var tagType: TagType
)