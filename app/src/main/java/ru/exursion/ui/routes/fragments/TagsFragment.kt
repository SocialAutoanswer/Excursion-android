package ru.exursion.ui.routes.fragments

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ru.exursion.R
import ru.exursion.ui.shared.TagsPagingAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class TagsFragment : BaseContentFragment() {

    override val adapter = TagsPagingAdapter {
        findNavController().navigate(
            R.id.fragment_routes,
            bundleOf(
                CITY_ID_BUNDLE_KEY to arguments?.getLong(CITY_ID_BUNDLE_KEY),
                TAG_ID_BUNDLE_KEY to it.id,
                TAG_NAME_BUNDLE_KEY to it.name,
            )
        )
    }
    override val titleResId: Int
        get() = if (viewModel.cityId != null)
                    R.string.screen_favorites_routes
                else
                    R.string.screen_tags_recommendations_title

    override fun getData() {
        viewModel.getRouteTags() // stub before recommendation added
    }
}
