package ru.exursion.ui.routes.fragments

import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.exursion.R
import ru.exursion.ui.routes.RouteDetailsActivity
import ru.exursion.ui.routes.adapter.RoutesDelegateAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class RoutesFragment: BaseContentFragment() {

    private val delegateAdapter = RoutesDelegateAdapter()

    override val adapter = CompositeDelegateAdapter(delegateAdapter)

    override val titleResId: Int
        get() = R.string.screen_town_routes_title

    override fun getData() {
        if (viewModel.isFavorite) {
            viewModel.getFavoriteRoutes()
        } else {
            viewModel.getRoutes()
        }
    }

    override fun readyCallback() {
        delegateAdapter.setOnItemClick {
            RouteDetailsActivity.start(activity, it.id, viewModel.tagId ?: return@setOnItemClick)
        }
    }
}