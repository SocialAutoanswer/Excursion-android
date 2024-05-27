package ru.bibaboba.kit.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.text.HtmlCompat

@SuppressLint("DiscouragedApi")
fun Context.getDrawableByName(name: String): Drawable? {
    val resourceId = resources.getIdentifier(name, "drawable", packageName)
    return try {
        AppCompatResources.getDrawable(this, resourceId)
    } catch (e: Resources.NotFoundException) {
        null
    }
}

@ColorInt
fun Context.getColorByAttr(@AttrRes attrId: Int): Int {
    val typedValue = TypedValue()
    theme?.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}

fun Context.getHtmlString(@StringRes stringId: Int, vararg arguments: Any): Spanned {
    return HtmlCompat.fromHtml(
        getString(stringId, *arguments),
        HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}