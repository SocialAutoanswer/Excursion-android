package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentHotelDetailsBinding
import ru.exursion.ui.routes.adapter.SocialMediaDelegateAdapter
import ru.exursion.ui.routes.vm.HotelDetailsViewModel
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class HotelDetailsFragment
    : StateFragment<FragmentHotelDetailsBinding, HotelDetailsViewModel>(FragmentHotelDetailsBinding::class.java) {

    companion object {
        const val HOTEL_ID_BUNDLE_KEY = "hotel_id"
    }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addReadyState()
        .addErrorEffect()
        .addLikeLoadingState()
        .addFavoriteStateChangedState()
        .build()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<HotelDetailsViewModel> { viewModelFactory }

    private val socialMediaAdapter = CompositeDelegateAdapter(SocialMediaDelegateAdapter())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun getStartData() {
        viewModel.hotelId = arguments?.getLong(HOTEL_ID_BUNDLE_KEY, -1) ?: -1
        viewModel.getHotelById()
    }

    private fun changeLikeButtonState(isLoading: Boolean) = with(binding) {
        likeLoading.isVisible = isLoading
        likeButton.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        likeButton.isEnabled = !isLoading
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(HotelDetailsViewModel.HotelDetailsState.Loading::class,
            callback = {
                binding.loading.root.isVisible = true
                binding.root.isEnabled = false
            },
            onExit = {
                binding.loading.root.isVisible = false
                binding.root.isEnabled = true
            },
        )
    }

    private fun StateMachine.Builder.addLikeLoadingState(): StateMachine.Builder {
        return addState(
            HotelDetailsViewModel.HotelDetailsState.LikeLoading::class,
            callback = { changeLikeButtonState(true) },
            onExit = { changeLikeButtonState(false) }
        )
    }

    private fun StateMachine.Builder.addFavoriteStateChangedState(): StateMachine.Builder {
        return addState(HotelDetailsViewModel.HotelDetailsState.FavoriteStateChanged::class) {
            binding.likeButton.isSelected = !binding.likeButton.isSelected
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(HotelDetailsViewModel.HotelDetailsState.Ready::class) {
            val hotelDetails = it.details

            with(binding) {
                hotelName.text = hotelDetails.name
                rating.rating = hotelDetails.rating
                address.text = hotelDetails.address
                description.text = hotelDetails.description
                likeButton.isSelected = hotelDetails.isFavorite

                binding.likeButton.setOnClickListener{ _ ->
                    viewModel.changeHotelFavoriteState(hotelDetails.id)
                }

                backButton.setOnClickListener { findNavController().navigateUp() }

                socialMediaRecycler.also {
                    it.adapter = socialMediaAdapter
                    it.addItemMargins(25, 16)
                }

                socialMediaAdapter.swapData(it.details.socialMedias)

                Glide.with(this@HotelDetailsFragment)
                    .load(hotelDetails.imageUrl)
                    .centerCrop()
                    .into(binding.backgroundImage)
            }
        }
    }

    private fun StateMachine.Builder.addErrorEffect() : StateMachine.Builder {
        return addEffect(HotelDetailsViewModel.HotelDetailsEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}