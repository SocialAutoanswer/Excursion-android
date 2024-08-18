package ru.exursion.ui.routes.fragments

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ru.exursion.R
import ru.exursion.ui.routes.adapter.HotelsPagingDataAdapter
import ru.exursion.ui.routes.fragments.HotelDetailsFragment.Companion.HOTEL_ID_BUNDLE_KEY
import ru.exursion.ui.shared.content.BaseContentFragment

class HotelsFragment: BaseContentFragment() {

    override val adapter = HotelsPagingDataAdapter()

    override val titleResId: Int
        get() = R.string.screen_favorites_hotels

    override fun getData() {
        if (viewModel.isFavorite) {
            viewModel.getFavoriteHotels()
        }
    }

    override fun onClearCLick() {
        viewModel.clearFavoriteHotels()
    }

    override fun readyCallback() {
        adapter.setOnItemClick {
            findNavController().navigate(R.id.hotelDetailsFragment, bundleOf(HOTEL_ID_BUNDLE_KEY to it.id))
        }
    }
}