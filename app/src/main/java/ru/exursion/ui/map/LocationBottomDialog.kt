package ru.exursion.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.text.method.ScrollingMovementMethod
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateBottomSheetDialogFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentLocationBottomSheetDialogBinding
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class LocationBottomDialog : StateBottomSheetDialogFragment<FragmentLocationBottomSheetDialogBinding, MapViewModel>(
    FragmentLocationBottomSheetDialogBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by activityViewModels<MapViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .enableDebugLogs()
        .addAudioLocationReceivedState()
        .addLikeLoadingState()
        .addFavoriteStateChangedState()
        .addErrorEffect()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setUpViews(view: View): Unit = with(binding) {
        // No questions, really
        dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            ?.setBackgroundColor(resources.getColor(R.color.transparent))

        routDescription.apply {
            // workaround to make text view scrollable in bottom sheet dialog
            movementMethod = ScrollingMovementMethod()
            setOnScrollChangeListener { _, _, _, _, _ -> bottomSheetDialog.behavior.isDraggable = false }
            setOnTouchListener { _, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> bottomSheetDialog.behavior.isDraggable = false
                    MotionEvent.ACTION_UP -> bottomSheetDialog.behavior.isDraggable = true
                }
                false
            }

        }


        player.playButton.setUiState(viewModel.getPointIsPlaying())
        player.setOnPlayerClickListener(viewModel.getPointPlayerClickListener())

        viewModel.setOnPlayerTimerListener(player::setCurrentPosition)
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
                    player.setDurationInSeconds(audio.durationInSeconds)
                    player.isVisible = true
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