package ru.exursion.ui.routes.fragments

import ru.exursion.R
import ru.exursion.ui.routes.adapter.EventsPagingDataAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class EventsFragment: BaseContentFragment() {

    override val adapter = EventsPagingDataAdapter()

    override val titleResId: Int
        get() =  R.string.screen_favorites_events

    override fun getData() {
        if (viewModel.isFavorite) {
            viewModel.getFavoriteEvents()
        }
    }

    override fun readyCallback() {
        //add click listener for item to open full info
    }

}