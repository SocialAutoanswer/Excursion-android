package ru.exursion.ui.routes.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.Hotel
import ru.exursion.databinding.ItemHotelBinding

class HotelsPagingDataAdapter :
    PagingDataAdapter<Hotel, ItemViewHolder<ItemHotelBinding, Hotel>>(HotelsDiffUtilCallback) {

    private var onItemClick: ((Hotel) -> Unit)? = null

    override fun onBindViewHolder(holder: ItemViewHolder<ItemHotelBinding, Hotel>, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemHotelBinding, Hotel> {
        return ItemViewHolder.create(parent) { binding, item, pos ->
            with(binding) {
                root.setOnClickListener { onItemClick?.invoke(item) }

                name.text = item.name
                address.text = item.address
                rating.rating = item.rating

                Glide.with(backgroundImage)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(backgroundImage)
            }
        }
    }

    fun setOnItemClick(callback: (Hotel) -> Unit) {
        onItemClick = callback
    }


}

private object HotelsDiffUtilCallback : DiffUtil.ItemCallback<Hotel>() {
    override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
        return oldItem == newItem
    }
}