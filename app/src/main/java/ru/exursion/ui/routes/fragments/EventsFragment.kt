package ru.exursion.ui.routes.fragments

import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.exursion.R
import ru.exursion.ui.routes.adapter.EventsDelegateAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class EventsFragment: BaseContentFragment() {

    override val adapter = CompositeDelegateAdapter(EventsDelegateAdapter())

    override val titleResId: Int
        get() = R.string.screen_favorites_events

    override fun getData() {
        if (viewModel.isFavorite) {
            viewModel.getFavoriteEvents()
        }
    }

    override fun readyCallback() {
        //add click listener for item to open full info
    }

}