package ru.exursion.ui.routes.fragments

import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.exursion.R
import ru.exursion.ui.routes.adapter.HotelsDelegateAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class HotelsFragment: BaseContentFragment() {

    private val delegateAdapter = HotelsDelegateAdapter()

    override val adapter = CompositeDelegateAdapter(delegateAdapter)

    override val titleResId: Int
        get() = R.string.screen_favorites_hotels

    override fun getData() {
        if (viewModel.isFavorite) {
            viewModel.getFavoriteHotels()
        }
    }

    override fun readyCallback() {
        //add click listener for item to open full info
    }
}