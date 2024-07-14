package ru.exursion.data.models

import com.google.gson.annotations.SerializedName

data class DetailDto (
    @SerializedName("detail") val detail: String?
)