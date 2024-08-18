package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class SocialMediaDto(
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("url") val socialUrl: String?,
    @SerializedName("picture") val iconName: String?
)

data class SocialMedia(
    val id: Long,
    val name: String,
    val socialUrl: String,
    val iconName: String
)