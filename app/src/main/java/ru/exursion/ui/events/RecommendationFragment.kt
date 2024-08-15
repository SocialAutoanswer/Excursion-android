package ru.exursion.ui.events

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import ru.exursion.R
import ru.exursion.data.models.TagType
import ru.exursion.ui.shared.TagsPagingAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class RecommendationFragment : BaseContentFragment() {

    override val adapter: PagingDataAdapter<*, *> = TagsPagingAdapter {
        val tagBundle = bundleOf(
            TAG_ID_BUNDLE_KEY to it.id,
            TAG_NAME_BUNDLE_KEY to it.name
        )

        when (it.tagType) {
            TagType.EVENTS -> findNavController().navigate(R.id.eventsFragment, tagBundle)
            TagType.ROUTES -> findNavController().navigate(R.id.routesFragment, tagBundle)
            TagType.LOCATIONS -> findNavController().navigate(R.id.locationsFragment, tagBundle)
        }
    }

    override var isBackButtonVisible = false

    override val titleResId: Int
        get() = R.string.screen_tags_recommendations_title

    override fun getData() {
        viewModel.getRecommendationsTags()
    }
}