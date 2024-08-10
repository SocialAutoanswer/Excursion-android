package ru.exursion.ui.routes.fragments

import android.util.Log
import ru.exursion.R
import ru.exursion.ui.routes.RouteDetailsActivity
import ru.exursion.ui.routes.adapter.RoutesPagingDataAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class RoutesFragment: BaseContentFragment() {

    override val adapter = RoutesPagingDataAdapter()

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
        adapter.setOnItemClick {
            RouteDetailsActivity.start(activity, it.id, viewModel.tagId ?: return@setOnItemClick)
        }
    }
}