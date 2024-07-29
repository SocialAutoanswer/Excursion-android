package ru.exursion.ui.shared

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.bibaboba.kit.ui.getDrawableByName
import ru.exursion.data.models.Tag
import ru.exursion.databinding.ItemSelectBinding


class TagsPagingAdapter(
    private val onTagClick: (Tag) -> Unit
) : PagingDataAdapter<Tag, ItemViewHolder<ItemSelectBinding, Tag>>(TagsDiffUtilCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder<ItemSelectBinding, Tag>, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemSelectBinding, Tag> {
        return ItemViewHolder.create(parent) { binding, tag, pos ->
            with(binding) {
                title.text = tag.name

                startIcon.context.getDrawableByName(tag.iconName)?.let {
                    startIcon.isVisible = true
                    startIcon.setImageDrawable(it)
                }

                root.setOnClickListener {
                    onTagClick(tag)
                }
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