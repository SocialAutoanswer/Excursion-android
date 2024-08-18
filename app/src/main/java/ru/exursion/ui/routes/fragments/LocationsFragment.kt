package ru.exursion.ui.routes.fragments

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ru.exursion.R
import ru.exursion.ui.routes.adapter.LocationsPagingDataAdapter
import ru.exursion.ui.routes.fragments.LocationDetailsFragment.Companion.LOCATION_ID_BUNDLE_KEY
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

    override fun readyCallback() {
        adapter.setOnItemClick {
            findNavController().navigate(R.id.locationDetailsFragment, bundleOf(LOCATION_ID_BUNDLE_KEY to it.id))
        }
    }
}