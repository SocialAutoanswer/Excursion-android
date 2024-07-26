package ru.exursion.ui.map

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.City
import ru.exursion.databinding.ItemCityNameBinding

class CityAdapter(
    private val onItemSelected: (City) -> Unit
): PagingDataAdapter<City, ItemViewHolder<ItemCityNameBinding, City>>(
    CityDiffUtilCallback
) {
    private var selectedView: View? = null


    override fun onBindViewHolder(
        holder: ItemViewHolder<ItemCityNameBinding, City>,
        position: Int
    ) {
        getItem(position)?.let{ holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemCityNameBinding, City> {
        return ItemViewHolder.create(parent) { binding, city ->
            binding.cityName.text = city.name

            binding.root.setOnClickListener {
                selectedView?.let { selectedView -> selectedView.isSelected = false }
                selectedView = it
                it.isSelected = true
                onItemSelected(city)
            }
        }
    }

}

private object CityDiffUtilCallback : DiffUtil.ItemCallback<City>() {
    override fun areItemsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: City, newItem: City): Boolean {
        return oldItem == newItem
    }
}