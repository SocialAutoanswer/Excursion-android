package ru.exursion.ui.routes.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.Event
import ru.exursion.databinding.ItemEventBinding

class EventsPagingDataAdapter : PagingDataAdapter<Event, ItemViewHolder<ItemEventBinding, Event>>(EventsDiffUtilCallback) {

    private var onItemClick: ((Event) -> Unit)? = null

    override fun onBindViewHolder(holder: ItemViewHolder<ItemEventBinding, Event>, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemEventBinding, Event> {
        return ItemViewHolder.create(parent) { binding, item, pos ->
            with(binding) {
                root.setOnClickListener { onItemClick?.invoke(item) }

                name.text = item.name
                address.text = item.address
                date.text = item.date

                Glide.with(backgroundImage)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(backgroundImage)
            }
        }
    }

    fun setOnItemClick(callback: (Event) -> Unit) {
        onItemClick = callback
    }


}

private object EventsDiffUtilCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}