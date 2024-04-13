package ru.exursion

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
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