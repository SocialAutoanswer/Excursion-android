package ru.exursion.ui.routes.vm

import androidx.paging.PagingData
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.Review
import javax.inject.Inject

class ReviewsViewModel @Inject constructor(): RxStateViewModel<ReviewsViewModel.ReviewsState, ReviewsViewModel.ReviewsEffect>() {

    fun requestReviews(routeId: Long) = invokeDisposable {

    }

    sealed class ReviewsState : State {
        data object Loading : ReviewsState()
        data object Idle : ReviewsState()
        data class Ready(val reviews: PagingData<Review>)
    }

    sealed class ReviewsEffect : Effect {
        data object Error : ReviewsEffect()
    }
}