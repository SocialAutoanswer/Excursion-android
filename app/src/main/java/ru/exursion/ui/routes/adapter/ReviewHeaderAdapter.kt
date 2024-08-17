package ru.exursion.ui.routes.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.R
import ru.exursion.databinding.SublayoutReviewHeaderBinding
import ru.exursion.ui.routes.RouteReviewHeader

class ReviewHeaderAdapter(private val rating: Float, private val reviewCount: Int): RecyclerView.Adapter<ItemViewHolder<SublayoutReviewHeaderBinding, RouteReviewHeader>>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<SublayoutReviewHeaderBinding, RouteReviewHeader> {
        return ItemViewHolder.create(parent) { binding, item, _ ->
            binding.rating.text = parent.context.getString(R.string.screen_route_reviews_rating, item.rating)
            binding.reviewsCount.text = parent.context.getString(R.string.screen_route_reviews_count, item.reviewCount)
        }
    }

    override fun getItemCount() = 1

    override fun onBindViewHolder(
        holder: ItemViewHolder<SublayoutReviewHeaderBinding, RouteReviewHeader>,
        position: Int
    ) {
        return holder.bind(RouteReviewHeader(rating, reviewCount), position)
    }


}