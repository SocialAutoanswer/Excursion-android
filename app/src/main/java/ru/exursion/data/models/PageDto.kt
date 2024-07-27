package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class PageDto<T> (
    @SerializedName("next") val nextPageLink: String?,
    @SerializedName("previous") val previousPageLink: String?,
    @SerializedName("data") val data: List<T?>?
)