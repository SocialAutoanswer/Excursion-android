package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentEventDetailsBinding
import ru.exursion.ui.routes.vm.EventDetailsViewModel
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class EventDetailsFragment: StateFragment<FragmentEventDetailsBinding, EventDetailsViewModel>(FragmentEventDetailsBinding::class.java) {

    companion object {
        const val EVENT_ID_BUNDLE_KEY = "event_id"
    }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addReadyState()
        .addErrorEffect()
        .addLikeLoadingState()
        .addFavoriteStateChangedState()
        .build()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<EventDetailsViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun getStartData() {
        viewModel.eventId = arguments?.getLong(EVENT_ID_BUNDLE_KEY, -1) ?: -1
        viewModel.getEventById()
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(EventDetailsViewModel.EventDetailsState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(EventDetailsViewModel.EventDetailsState.Ready::class) {
            val eventDetails = it.details

            with(binding) {
                eventName.text = eventDetails.name
                date.text = eventDetails.date
                address.text = eventDetails.address

                binding.likeButton.setOnClickListener{ _ ->
                    viewModel.changeEventFavoriteState(eventDetails.id)
                }

                Glide.with(this@EventDetailsFragment)
                    .load(eventDetails.imageUrl)
                    .centerCrop()
                    .into(binding.backgroundImage)
            }
        }
    }

    private fun changeLikeButtonState(isLoading: Boolean) = with(binding) {
        likeLoading.isVisible = isLoading
        likeButton.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        likeButton.isEnabled = !isLoading
    }

    private fun StateMachine.Builder.addLikeLoadingState(): StateMachine.Builder {
        return addState(EventDetailsViewModel.EventDetailsState.LikeLoading::class,
            callback = { changeLikeButtonState(true) },
            onExit = { changeLikeButtonState(false) }
        )
    }

    private fun StateMachine.Builder.addFavoriteStateChangedState(): StateMachine.Builder {
        return addState(EventDetailsViewModel.EventDetailsState.FavoriteStateChanged::class) {
            binding.likeButton.isSelected = !binding.likeButton.isSelected
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addErrorEffect() : StateMachine.Builder {
        return addEffect(EventDetailsViewModel.EventDetailsEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}