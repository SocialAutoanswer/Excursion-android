package ru.exursion.ui.routes.fragments

import android.view.View
import androidx.recyclerview.widget.ConcatAdapter
import ru.exursion.R
import ru.exursion.ui.routes.adapter.ReviewHeaderAdapter
import ru.exursion.ui.routes.adapter.ReviewsPagingDataAdapter
import ru.exursion.ui.shared.content.BaseContentFragment

class ReviewsFragment: BaseContentFragment() {
    override val adapter = ReviewsPagingDataAdapter()

    override val titleResId = R.string.screen_route_details_reviews

    override fun getData() {
        viewModel.getRouteReviews()
    }

    override fun setUpViews(view: View) {
        super.setUpViews(view)
        val concatAdapter = ConcatAdapter(ReviewHeaderAdapter(1.1f, 12), adapter) //TODO:change mock data
        binding.recycler.adapter = concatAdapter
    }
}