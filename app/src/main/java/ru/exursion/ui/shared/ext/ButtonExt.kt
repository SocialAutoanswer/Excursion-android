package ru.exursion.ui.shared.ext

import android.graphics.drawable.Drawable
import android.widget.Button
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import ru.exursion.R

fun Button.setStartDrawable(@DrawableRes iconResId: Int?) {
    iconResId?.let {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            AppCompatResources.getDrawable(context, it),
            compoundDrawablesRelative[1],
            compoundDrawablesRelative[2],
            compoundDrawablesRelative[3]
        )
    } ?: run {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            AppCompatResources.getDrawable(context, R.drawable.ic_cross),
            compoundDrawablesRelative[1],
            compoundDrawablesRelative[2],
            compoundDrawablesRelative[3]
        )
    }
}

fun Button.setStartDrawable(icon: Drawable?) {
    icon?.let {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            icon,
            compoundDrawablesRelative[1],
            compoundDrawablesRelative[2],
            compoundDrawablesRelative[3]
        )
    } ?: run {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            AppCompatResources.getDrawable(context, R.drawable.ic_cross),
            compoundDrawablesRelative[1],
            compoundDrawablesRelative[2],
            compoundDrawablesRelative[3]
        )
    }
}