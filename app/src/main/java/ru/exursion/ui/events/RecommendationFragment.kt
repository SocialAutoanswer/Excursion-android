package ru.exursion.ui.events

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingDataAdapter
import ru.bibaboba.kit.ui.getHtmlString
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
    override var isEndTextVisible = true

    override val titleResId: Int
        get() = R.string.screen_tags_recommendations_title

    private val citiesAdapter by lazy {
        CitiesPopupAdapter(requireContext()) {
            binding.header.endText.text = context?.getHtmlString(R.string.underlined_text, it)
            viewModel.cityName = it
            viewModel.getRecommendationsTags()
        }
    }

    override fun getData() {
        viewModel.citiesLiveData.observe(viewLifecycleOwner) {
            citiesAdapter.clear()
            val names = it.map { it.name }
            citiesAdapter.addAll(names)
            viewModel.cityName = names.firstOrNull()
            binding.header.endText.text = context?.getHtmlString(R.string.underlined_text, names.firstOrNull() ?: "")
            viewModel.getRecommendationsTags()
        }

        viewModel.getCitiesPage()
    }

    override fun setUpViews(view: View) {
        super.setUpViews(view)

        val popup = ListPopupWindow(requireContext()).apply {
            anchorView = binding.header.endText
            setAdapter(citiesAdapter)
            setBackgroundDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.shape_rectangle))
            width = 450
        }

        binding.header.endText.setOnClickListener { popup.show() }
    }
}