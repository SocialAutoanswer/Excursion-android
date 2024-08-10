package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class TagDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("picture") val picture: String?,
    @SerializedName("tags") val tags: List<Long?>?,
    @SerializedName("routes") val routes: List<Long?>?,
    @SerializedName("type") val type: Char
)

data class Tag(
    val id: Long,
    val name: String,
    val iconName: String,
    val routes: List<Long>
)