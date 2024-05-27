package ru.bibaboba.kit.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.bibaboba.kit.ui.utils.inflateBinding
import java.lang.Exception

open class ItemViewHolder<B: ViewBinding, T>(
    view: View,
    private val binding: B,
    private val onBind: ((B, T) -> Unit)? = null
) : RecyclerView.ViewHolder(view) {

    companion object {
        inline fun <reified B: ViewBinding, T> create(
            parent: ViewGroup,
            noinline bind: ((B, T) -> Unit)? = null
        ): ItemViewHolder<B, T> {
            val binding = B::class.java
                .inflateBinding(LayoutInflater.from(parent.context), parent) ?: throw Exception("Cannot create view holder")

            return ItemViewHolder(binding.root, binding, bind)
        }
    }

    fun bind(item: T) {
        onBind?.invoke(binding, item)
    }
}