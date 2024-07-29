package ru.exursion.ui.routes.adapter

import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.Review
import ru.exursion.databinding.ItemReviewBinding

class ReviewsDelegateAdapter : ViewBindingDelegateAdapter<Review, ItemReviewBinding>(ItemReviewBinding::inflate) {

    override fun isForViewType(item: Any) = item is Review

    override fun ItemReviewBinding.onBind(item: Review) {
        userName.text = "Отзовиков отзыв"
        rating.rating = item.rating.toFloat()
        reviewSummary.text = item.reviewText
    }

    override fun Review.getItemId(): Any = id
}