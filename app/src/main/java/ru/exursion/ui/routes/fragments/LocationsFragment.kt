package ru.exursion.ui.routes.fragments

import androidx.paging.PagingDataAdapter
import ru.exursion.R
import ru.exursion.ui.routes.adapter.LocationsPagingDataAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class LocationsFragment: BaseContentFragment() {
    override val adapter: PagingDataAdapter<*, *>
        get() = LocationsPagingDataAdapter()

    override val titleResId: Int
        get() = R.string.screen_locations_title

    override fun getData() {
        viewModel.getLocations()
    }


}