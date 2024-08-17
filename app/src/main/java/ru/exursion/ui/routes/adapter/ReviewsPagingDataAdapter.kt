package ru.exursion.ui.routes.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.exursion.data.models.Review
import ru.exursion.databinding.ItemReviewBinding

class ReviewsPagingDataAdapter: PagingDataAdapter<Review, ItemViewHolder<ItemReviewBinding, Review>>(ReviewsDiffUtilCallback) {

     override fun onBindViewHolder(
        holder: ItemViewHolder<ItemReviewBinding, Review>,
        position: Int
    ) {
         getItem(position)?.let { holder.bind(it, position) }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemReviewBinding, Review> {
        return ItemViewHolder.create(parent) { binding, item, pos ->
            with(binding) {
                userName.text = ""
                rating.rating = item.rating.toFloat()
                reviewSummary.text = item.reviewText
            }
        }
    }


}

private object ReviewsDiffUtilCallback : DiffUtil.ItemCallback<Review>() {
    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}