package ru.bibaboba.kit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources

@SuppressLint("DiscouragedApi")
fun Context.getDrawableByName(name: String): Drawable? {
    val resourceId = resources.getIdentifier(name, "drawable", packageName)
    return try {
        AppCompatResources.getDrawable(this, resourceId)
    } catch (e: Resources.NotFoundException) {
        null
    }
}