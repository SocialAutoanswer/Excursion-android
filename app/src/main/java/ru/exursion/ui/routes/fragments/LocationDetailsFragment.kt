package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentLocationDetailsBinding
import ru.exursion.ui.routes.vm.LocationDetailsViewModel
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class LocationDetailsFragment
    : StateFragment<FragmentLocationDetailsBinding, LocationDetailsViewModel>(FragmentLocationDetailsBinding::class.java) {


    companion object {
        const val LOCATION_ID_BUNDLE_KEY = "location_id"
    }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addReadyState()
        .addErrorEffect()
        .addLikeLoadingState()
        .addFavoriteStateChangedState()
        .build()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<LocationDetailsViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun getStartData() {
        viewModel.locationId = arguments?.getLong(LOCATION_ID_BUNDLE_KEY, -1) ?: -1
        viewModel.getLocationById()
    }

    private fun changeLikeButtonState(isLoading: Boolean) = with(binding) {
        likeLoading.isVisible = isLoading
        likeButton.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        likeButton.isEnabled = !isLoading
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            LocationDetailsViewModel.LocationDetailsState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addLikeLoadingState(): StateMachine.Builder {
        return addState(
            LocationDetailsViewModel.LocationDetailsState.LikeLoading::class,
            callback = { changeLikeButtonState(true) },
            onExit = { changeLikeButtonState(false) }
        )
    }

    private fun StateMachine.Builder.addFavoriteStateChangedState(): StateMachine.Builder {
        return addState(LocationDetailsViewModel.LocationDetailsState.FavoriteStateChanged::class) {
            binding.likeButton.isSelected = !binding.likeButton.isSelected
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(LocationDetailsViewModel.LocationDetailsState.Ready::class) {
            val locationDetails = it.details

            with(binding) {
                locationName.text = locationDetails.name
                description.text = locationDetails.description
                likeButton.isSelected = locationDetails.isFavorite

                binding.likeButton.setOnClickListener{ _ ->
                    viewModel.changeLocationFavoriteState(locationDetails.id)
                }
                backButton.setOnClickListener { findNavController().navigateUp() }

                try {
                    Glide.with(this@LocationDetailsFragment)
                        .load(locationDetails.photos[0].url)
                        .centerCrop()
                        .into(binding.backgroundImage)
                } catch (_: IndexOutOfBoundsException) { }

            }
        }
    }

    private fun StateMachine.Builder.addErrorEffect() : StateMachine.Builder {
        return addEffect(LocationDetailsViewModel.LocationDetailsEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}