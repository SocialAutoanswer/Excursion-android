package ru.exursion.ui.map

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.bibaboba.kit.states.StateMachine
import ru.exursion.databinding.FragmentBottomSheetExcBinding
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class LocationBottomDialog: BottomSheetDialogFragment() {

    private var _binding: FragmentBottomSheetExcBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by activityViewModels<MapViewModel> { viewModelFactory }

    private var onDismissCallback: (() -> Unit)? = null

    private val stateMachine = StateMachine.Builder()
        .addAudioLocationReceivedState()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentBottomSheetExcBinding.inflate(inflater, container, false).also { _binding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()

        binding.player.setPlayerUI(viewModel.getDoIPlay())
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

    private fun StateMachine.Builder.addAudioLocationReceivedState(): StateMachine.Builder {
        return addState(MapViewModel.MapState.AudioLocationReceived::class) {
            val audio = it.audioLocation.audios[0]

            with(binding) {
                routName.text = it.audioLocation.name
                routDescription.text = it.audioLocation.description

                player.setTrackName(audio.name)
                //player.setDuration(audio.duration)

                likeButton.isSelected = it.audioLocation.isFavorite
                likeButton.setOnClickListener{ likeButton ->
                    likeButton.isSelected = !likeButton.isSelected
                    //viewModel.addToFavorite(it.audioLocation.id)
                }
            }
        }
    }

}