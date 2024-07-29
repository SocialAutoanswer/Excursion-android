package ru.exursion.ui.map

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.City
import ru.exursion.databinding.ItemCityNameBinding

class CityAdapter(
    private val onItemSelected: (City) -> Unit,
    var currentPosition: Int? = null
): PagingDataAdapter<City, ItemViewHolder<ItemCityNameBinding, City>>(
    CityDiffUtilCallback
) {
    private var selectedView: View? = null


    override fun onBindViewHolder(
        holder: ItemViewHolder<ItemCityNameBinding, City>,
        position: Int
    ) {
        getItem(position)?.let{ holder.bind(it, position) }

        if (position == currentPosition) onItemClick(holder.itemView)

        if (currentPosition == null) {
            onItemClick(holder.itemView, 0)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemCityNameBinding, City> {
        return ItemViewHolder.create(parent) { binding, city, pos ->
            binding.cityName.text = city.name

            binding.root.setOnClickListener {
                onItemClick(it, pos)
            }
        }
    }

    private fun onItemClick(view: View, position: Int) {
        onItemClick(view)
        onItemSelected(getItem(position) ?: return)
        currentPosition = position
    }

    private fun onItemClick(view: View) {
        selectedView?.let { selectedView -> selectedView.isSelected = false }
        selectedView = view
        view.isSelected = true
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