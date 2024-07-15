package ru.exursion.ui.routes

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.Route
import ru.exursion.databinding.ItemRouteBinding

class RoutesPagingDataAdapter(
    private val onClick: (Route) -> Unit,
) : PagingDataAdapter<Route, ItemViewHolder<ItemRouteBinding, Route>>(RoutesDiffUtilCallback) {
    override fun onBindViewHolder(holder: ItemViewHolder<ItemRouteBinding, Route>, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemRouteBinding, Route> {
        return ItemViewHolder.create(parent) { binding, item ->
            with(binding) {
                root.setOnClickListener { onClick(item) }

                name.text = item.name
                description.text = item.description

                Glide.with(binding.backgroundImage)
                    .load(item.image)
                    .centerCrop()
                    .into(binding.backgroundImage)
            }
        }
    }


}

private object RoutesDiffUtilCallback : DiffUtil.ItemCallback<Route>() {
    override fun areItemsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Route, newItem: Route): Boolean {
        return oldItem == newItem
    }
}