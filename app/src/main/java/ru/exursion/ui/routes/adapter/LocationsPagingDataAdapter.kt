package ru.exursion.ui.routes.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.Location
import ru.exursion.databinding.ItemLocationBinding

class LocationsPagingDataAdapter : PagingDataAdapter<AudioLocation, ItemViewHolder<ItemLocationBinding, AudioLocation>>(LocationsDiffUtilCallback) {

    private var onItemClick: ((AudioLocation) -> Unit)? = null

    override fun onBindViewHolder(holder: ItemViewHolder<ItemLocationBinding, AudioLocation>, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemLocationBinding, AudioLocation> {
        return ItemViewHolder.create(parent) { binding, item, pos ->
            with(binding) {
                root.setOnClickListener { onItemClick?.invoke(item) }

                name.text = item.name
                description.text = item.description

                Glide.with(backgroundImage)
                    .load(item.photos.firstOrNull()?.url ?: return@with)
                    .centerCrop()
                    .into(backgroundImage)
            }
        }
    }

    fun setOnItemClick(callback: (AudioLocation) -> Unit) {
        onItemClick = callback
    }


}

private object LocationsDiffUtilCallback : DiffUtil.ItemCallback<AudioLocation>() {
    override fun areItemsTheSame(oldItem: AudioLocation, newItem: AudioLocation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AudioLocation, newItem: AudioLocation): Boolean {
        return oldItem == newItem
    }
}