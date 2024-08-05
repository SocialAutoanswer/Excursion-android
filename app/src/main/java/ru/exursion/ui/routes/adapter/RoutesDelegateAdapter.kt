package ru.exursion.ui.routes.adapter

import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.Route
import ru.exursion.databinding.ItemRouteBinding

class RoutesDelegateAdapter
    : ViewBindingDelegateAdapter<Route, ItemRouteBinding>(ItemRouteBinding::inflate) {

    private var onClick: ((Route) -> Unit)? = null

    fun setOnItemClick(callback: (Route) -> Unit) {
        onClick = callback
    }

    override fun isForViewType(item: Any) = item is Route

    override fun ItemRouteBinding.onBind(item: Route) {
        root.setOnClickListener { onClick?.invoke(item) }

        name.text = item.name
        description.text = item.description

        Glide.with(backgroundImage)
            .load(item.image)
            .centerCrop()
            .into(backgroundImage)
    }

    override fun Route.getItemId(): Any = id
}