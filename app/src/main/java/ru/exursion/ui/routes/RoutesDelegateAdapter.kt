package ru.exursion.ui.routes

import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.Route
import ru.exursion.databinding.ItemRouteBinding

class RoutesDelegateAdapter(
    private val onClick: (Route) -> Unit
) : ViewBindingDelegateAdapter<Route, ItemRouteBinding>(ItemRouteBinding::inflate) {

    override fun isForViewType(item: Any) = item is Route

    override fun ItemRouteBinding.onBind(item: Route) {
        root.setOnClickListener { onClick(item) }

        name.text = item.name
        description.text = item.description

        Glide.with(backgroundImage)
            .load(item.image)
            .centerCrop()
            .into(backgroundImage)
    }

    override fun Route.getItemId(): Any = id
}