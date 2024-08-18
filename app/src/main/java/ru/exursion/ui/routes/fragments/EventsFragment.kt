package ru.exursion.ui.routes.fragments

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ru.exursion.R
import ru.exursion.ui.routes.adapter.EventsPagingDataAdapter
import ru.exursion.ui.routes.fragments.EventDetailsFragment.Companion.EVENT_ID_BUNDLE_KEY
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

    override fun onClearCLick() {
        viewModel.clearFavoriteEvents()
    }

    override fun readyCallback() {
        adapter.setOnItemClick {
            findNavController().navigate(R.id.eventDetailsFragment, bundleOf(EVENT_ID_BUNDLE_KEY to it.id))
        }
    }

}