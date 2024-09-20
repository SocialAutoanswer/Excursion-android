package ru.exursion.ui.routes.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.Location
import ru.exursion.databinding.ItemLocationBinding

class LocationsPagingDataAdapter : PagingDataAdapter<Location, ItemViewHolder<ItemLocationBinding, Location>>(LocationsDiffUtilCallback) {

    private var onItemClick: ((Location) -> Unit)? = null

    override fun onBindViewHolder(holder: ItemViewHolder<ItemLocationBinding, Location>, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemLocationBinding, Location> {
        return ItemViewHolder.create(parent) { binding, item, pos ->
            with(binding) {
                root.setOnClickListener { onItemClick?.invoke(item) }

                name.text = item.name
                description.text = item.description

                Glide.with(backgroundImage)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(backgroundImage)
            }
        }
    }

    fun setOnItemClick(callback: (Location) -> Unit) {
        onItemClick = callback
    }


}

private object LocationsDiffUtilCallback : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
        return oldItem == newItem
    }
}