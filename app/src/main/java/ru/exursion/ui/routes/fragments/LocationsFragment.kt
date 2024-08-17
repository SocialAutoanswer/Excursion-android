package ru.exursion.ui.routes.fragments

import ru.exursion.R
import ru.exursion.ui.routes.adapter.LocationsPagingDataAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class LocationsFragment: BaseContentFragment() {
    override val adapter = LocationsPagingDataAdapter()

    override val titleResId: Int
        get() = R.string.screen_locations_title

    override fun getData() {
        if (viewModel.isFavorite) {
            viewModel.getFavoriteLocations()
        } else {
            viewModel.getLocations()
        }
    }

    override fun onClearCLick() {
        viewModel.clearFavoriteLocations()
    }
}