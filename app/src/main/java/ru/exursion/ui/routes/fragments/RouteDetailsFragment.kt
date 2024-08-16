package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.bibaboba.kit.ui.getColorByAttr
import ru.bibaboba.kit.ui.getHtmlString
import ru.exursion.R
import ru.exursion.databinding.FragmentRouteDetailsBinding
import ru.exursion.ui.routes.RouteDetailsActivity
import ru.exursion.ui.routes.adapter.ReviewsDelegateAdapter
import ru.exursion.ui.routes.vm.RouteDetailsViewModel
import ru.exursion.ui.shared.ext.addItemDivider
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class RouteDetailsFragment : StateFragment<FragmentRouteDetailsBinding, RouteDetailsViewModel>(
    FragmentRouteDetailsBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<RouteDetailsViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addLoadingState()
        .addReadyState()
        .addErrorEffect()
        .addLikeLoadingState()
        .addFavoriteStateChangedState()
        .build()

    private val adapter = CompositeDelegateAdapter(
        ReviewsDelegateAdapter()
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun getStartData() {
        val activity = (activity as RouteDetailsActivity?) ?: return
        val routeId = activity.routeId ?: return
        viewModel.getRouteDetails(routeId)
    }

    override fun setUpViews(view: View) = with(binding) {
        backButton.setOnClickListener { activity?.onBackPressed() }
        reviewsRecycler.adapter = adapter
        context?.getColorByAttr(R.attr.exc_divider_color)?.let {
            reviewsRecycler.addItemDivider(it)
        }

        val primaryColor = context?.getColorByAttr(R.attr.exc_color_primary).toString()
        binding.showAll.text = context?.getHtmlString(R.string.screen_route_details_show_all, primaryColor)

        showAll.isVisible = false
        reviewsRecycler.isVisible = false
        reviewsTitle.isVisible = false
    }

    private fun changeLikeButtonState(isLoading: Boolean) = with(binding) {
        likeLoading.isVisible = isLoading
        likeButton.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        likeButton.isEnabled = !isLoading
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(RouteDetailsViewModel.RouteDetailsState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addLikeLoadingState(): StateMachine.Builder {
        return addState(RouteDetailsViewModel.RouteDetailsState.LikeLoading::class,
            callback = { changeLikeButtonState(true) },
            onExit = { changeLikeButtonState(false) }
        )
    }

    private fun StateMachine.Builder.addFavoriteStateChangedState(): StateMachine.Builder {
        return addState(RouteDetailsViewModel.RouteDetailsState.FavoriteStateChanged::class) {
            binding.likeButton.isSelected = !binding.likeButton.isSelected
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addReadyState() : StateMachine.Builder {
        return addState(RouteDetailsViewModel.RouteDetailsState.Ready::class) {
            val routeDetails = it.details

            if (it.details.reviews.isNotEmpty()) {
                binding.showAll.isVisible = true
                binding.reviewsRecycler.isVisible = true
                binding.reviewsTitle.isVisible = true
            }

            binding.likeButton.isSelected = routeDetails.isFavorite

            binding.likeButton.setOnClickListener{ _ ->
                viewModel.changeRouteFavoriteState(routeDetails.id)
            }

            binding.routeName.text = routeDetails.name
            binding.duration.text = getString(R.string.screen_route_details_duration, routeDetails.durationInMinutes)
            binding.locationsAmount.text = resources.getQuantityString(R.plurals.screen_route_details_locations_amount, routeDetails.locations.size, routeDetails.locations.size)
            binding.kilometers.text = getString(R.string.screen_route_details_kilometers, routeDetails.kilometers)
            binding.description.text = routeDetails.description

            adapter.swapData(it.details.reviews)

            Glide.with(this@RouteDetailsFragment)
                .load(routeDetails.image)
                .centerCrop()
                .into(binding.backgroundImage)
        }
    }

    private fun StateMachine.Builder.addErrorEffect() : StateMachine.Builder {
        return addEffect(RouteDetailsViewModel.RouteDetailsEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}