package ru.exursion.ui.routes.fragments

import ru.exursion.R
import ru.exursion.ui.routes.adapter.HotelsPagingDataAdapter
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
        //add click listener for item to open full info
    }
}