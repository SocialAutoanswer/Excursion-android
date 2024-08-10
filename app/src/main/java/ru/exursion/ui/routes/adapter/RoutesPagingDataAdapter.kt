package ru.exursion.ui.routes.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.Route
import ru.exursion.databinding.ItemRouteBinding

class RoutesPagingDataAdapter
 : PagingDataAdapter<Route, ItemViewHolder<ItemRouteBinding, Route>>(RoutesDiffUtilCallback) {

    private var onItemClick: ((Route) -> Unit)? = null

    override fun onBindViewHolder(holder: ItemViewHolder<ItemRouteBinding, Route>, position: Int) {
        getItem(position)?.let { holder.bind(it, position) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemRouteBinding, Route> {
        return ItemViewHolder.create(parent) { binding, item, pos ->
            with(binding) {
                root.setOnClickListener { onItemClick?.invoke(item) }

                name.text = item.name
                description.text = item.description

                Glide.with(binding.backgroundImage)
                    .load(item.imageUrl)
                    .centerCrop()
                    .into(binding.backgroundImage)
            }
        }
    }

    fun setOnItemClick(callback: (Route) -> Unit) {
        onItemClick = callback
    }


}

private object RoutesDiffUtilCallback : DiffUtil.ItemCallback<Route>() {
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem == newItem
    }
}