package ru.exursion.ui.shared

import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.bibaboba.kit.ui.getDrawableByName
import ru.exursion.R
import ru.exursion.data.models.TagItem
import ru.exursion.databinding.ItemSelectBinding
import ru.exursion.ui.shared.ext.setDrawable

class TagsPagingAdapter(
    private val onTagClick: (TagItem) -> Unit
) : PagingDataAdapter<TagItem, ItemViewHolder<ItemSelectBinding, TagItem>>(TagsDiffUtilCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder<ItemSelectBinding, TagItem>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemSelectBinding, TagItem> {
        return ItemViewHolder.create(parent) { binding, tag ->
            with(binding) {
                root.text = tag.name

                root.setOnClickListener { onTagClick(tag) }
            }
        }
    }

}

private object TagsDiffUtilCallback : DiffUtil.ItemCallback<TagItem>() {
    override fun areItemsTheSame(oldItem: TagItem, newItem: TagItem): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: TagItem, newItem: TagItem): Boolean {
        return oldItem == newItem
    }
}