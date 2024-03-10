package ru.bibaboba.kit.ui.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

const val LOG_TAG = "BindingUtils"

fun <VB: ViewBinding> Class<VB>.inflateBinding(inflater: LayoutInflater, parent: ViewGroup?): VB? {
    return try {
        val bindingInflate = getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)

        bindingInflate.invoke(null, inflater, parent, false) as VB
    } catch (e: Exception) {
        Log.d(LOG_TAG, "Binding inflate failed: ", e)
        null
    }
}

inline fun <reified VB: ViewBinding> inflateBinding(inflater: LayoutInflater): VB? {
    return try {
        val bindingInflate = VB::class.java.getMethod("inflate", LayoutInflater::class.java)

        bindingInflate.invoke(null, inflater) as VB
    } catch (e: Exception) {
        Log.d(LOG_TAG, "Binding inflate failed: ", e)
        null
    }
}