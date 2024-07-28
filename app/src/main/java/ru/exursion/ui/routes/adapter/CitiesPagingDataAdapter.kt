package ru.exursion.ui.routes.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.City
import ru.exursion.databinding.ItemTownBinding

class CitiesPagingDataAdapter(
    private val onCityClick: (City) -> Unit
) : PagingDataAdapter<City, ItemViewHolder<ItemTownBinding, City>>(CitiesDiffUtilCallback) {

    override fun onBindViewHolder(holder: ItemViewHolder<ItemTownBinding, City>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<ItemTownBinding, City> {
        return ItemViewHolder.create<ItemTownBinding, City>(parent) { binding, item ->
            binding.townName.text = item.name
            binding.root.setOnClickListener { onCityClick(item) }

            Glide.with(binding.backgroundImage)
                .load(item.image)
                .centerCrop()
                .into(binding.backgroundImage)
        }
    }
}

private object CitiesDiffUtilCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}