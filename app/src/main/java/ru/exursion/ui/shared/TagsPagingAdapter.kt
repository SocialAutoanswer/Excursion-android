package ru.exursion.ui.shared

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.bibaboba.kit.ui.getDrawableByName
import ru.exursion.R
import ru.exursion.data.models.Tag
import ru.exursion.databinding.ItemSelectBinding
import ru.exursion.ui.shared.ext.setDrawable


class TagsPagingAdapter(
    private val onTagClick: (Tag) -> Unit
) : PagingDataAdapter<Tag, ItemViewHolder<ItemSelectBinding, Tag>>(TagsDiffUtilCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder<ItemSelectBinding, Tag>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemSelectBinding, Tag> {
        return ItemViewHolder.create(parent) { binding, tag ->
            with(binding) {
                title.text = tag.name

                startIcon.isVisible = true

                startIcon.context.getDrawableByName(tag.iconName)?.let {
                    startIcon.setImageDrawable(it)
                } ?: run { startIcon.setDrawable(R.drawable.ic_cross) }

                root.setOnClickListener { onTagClick(tag) }
            }
        }
    }

}

private object TagsDiffUtilCallback : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem == newItem
    }
}