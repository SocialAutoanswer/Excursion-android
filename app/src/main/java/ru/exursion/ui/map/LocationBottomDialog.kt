package ru.exursion.ui.map

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.bibaboba.kit.states.StateMachine
import ru.exursion.R
import ru.exursion.databinding.FragmentLocationBottomSheetDialogBinding
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class LocationBottomDialog: BottomSheetDialogFragment() {

    private var _binding: FragmentLocationBottomSheetDialogBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by activityViewModels<MapViewModel> { viewModelFactory }

    private var onDismissCallback: (() -> Unit)? = null

    private val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addAudioLocationReceivedState()
        .addLikeLoadingState()
        .addFavoriteStateChangedState()
        .addErrorEffect()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentLocationBottomSheetDialogBinding.inflate(inflater, container, false).also { _binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()

        // No questions, really
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundColor(resources.getColor(R.color.transparent))

        binding.player.setPlayerUI(viewModel.getPointIsPlaying())
        binding.player.setOnPlayerClickListener(viewModel.getOnPlayerClickListener())
        viewModel.setOnPlayerTimerListener(binding.player::setCurrentPosition)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.invoke()
    }

    fun setOnDismiss(callback: () -> Unit) {
        onDismissCallback = callback
    }

    private fun setUpObservers() {
        viewModel.state.observe(viewLifecycleOwner, stateMachine::submit)
        viewModel.effect.observe(viewLifecycleOwner, stateMachine::submit)
    }

    private fun changeLikeButtonState(isLoading: Boolean) = with(binding) {
        likeLoading.isVisible = isLoading
        likeButton.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
        likeButton.isEnabled = !isLoading
    }

    private fun StateMachine.Builder.addAudioLocationReceivedState(): StateMachine.Builder {
        return addState(MapViewModel.MapState.AudioLocationReceived::class) {

            with(binding) {
                routName.text = it.audioLocation.name
                routDescription.text = it.audioLocation.description

                likeButton.isSelected = it.audioLocation.isFavorite

                likeButton.setOnClickListener{ _ ->
                    viewModel.changeLocationFavoriteState(it.audioLocation.id)
                }

                if (it.audioLocation.audios.isNotEmpty()) {
                    val audio = it.audioLocation.audios[0]
                    player.setTrackName(audio.name)
                    //player.setDuration(audio.duration)
                } else {
                    player.isVisible = false
                }

                if (it.audioLocation.photos.isEmpty()) return@addState

                Glide.with(context ?: return@addState)
                    .load(it.audioLocation.photos[0].url)
                    .centerCrop()
                    .error(R.drawable.ic_cross)
                    .into(binding.locationImage)
            }
        }
    }

    private fun StateMachine.Builder.addLikeLoadingState(): StateMachine.Builder {
        return addState(MapViewModel.MapState.LikeLoading::class,
            callback = { changeLikeButtonState(true) },
            onExit = { changeLikeButtonState(false) }
        )
    }


    private fun StateMachine.Builder.addFavoriteStateChangedState(): StateMachine.Builder {
        return addState(MapViewModel.MapState.FavoriteStateChanged::class) {
            binding.likeButton.isSelected = !binding.likeButton.isSelected
            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(MapViewModel.MapEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_LONG).show()
        }
    }

}