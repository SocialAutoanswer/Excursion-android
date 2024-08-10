package ru.exursion.ui.shared

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.bibaboba.kit.ui.getDrawableByName
import ru.exursion.data.models.Tag
import ru.exursion.databinding.ItemSelectBinding
import ru.exursion.ui.shared.ext.setStartDrawable

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
        return ItemViewHolder.create(parent) { binding, tag, _ ->
            with(binding) {
                root.text = tag.name

                root.setStartDrawable(
                    root.context.getDrawableByName(tag.iconName)
                )

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