package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class TagPageDto(
    @SerializedName("next") val nextPage: String?,
    @SerializedName("previous") val previousPage: String?,
    @SerializedName("data") val tags: List<TagDto?>?
)

data class TagDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("picture") val picture: String?,
    @SerializedName("tags") val tags: List<Int?>?,
    @SerializedName("routes") val routes: List<Int?>?
)

data class Tag(
    val id: Int,
    val name: String,
    val iconName: String,
    val tagsIds: List<Int>,
    val routesIds: List<Int>
)