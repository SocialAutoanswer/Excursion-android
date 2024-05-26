package ru.exursion.ui.shared.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

fun ImageView.startAnimatedVectorDrawable(@DrawableRes animationId: Int, onEnd: (Drawable?) -> Unit = {}) {
    val animation = AnimatedVectorDrawableCompat.create(context, animationId)?.apply {
        registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) = onEnd.invoke(drawable)
        })
    }
    setImageDrawable(animation)
    animation?.start()
}

fun ImageView.setDrawable(@DrawableRes drawableId: Int) = setImageDrawable(AppCompatResources.getDrawable(context, drawableId))