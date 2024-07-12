package ru.exursion.ui.shared.ext

import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.annotation.DrawableRes

fun Button.setDrawableStart(@DrawableRes icon:  Int) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, icon, 0);
}

fun Button.setDrawableStart(icon:  Drawable) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon,  null);
}