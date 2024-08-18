package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.map.MapObjectTapListener
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.databinding.FragmentRouteMapBinding
import ru.exursion.ui.routes.RouteActivityController
import ru.exursion.ui.routes.vm.RouteDetailsViewModel
import ru.exursion.ui.shared.ext.MarkType
import ru.exursion.ui.shared.ext.PlaceMark
import ru.exursion.ui.shared.ext.createRoute
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class RouteMapFragment: BaseFragment<FragmentRouteMapBinding>(FragmentRouteMapBinding::class.java) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by activityViewModels<RouteDetailsViewModel> { viewModelFactory }

    private val objectTapListener = MapObjectTapListener { _, _ ->
        openAudiosDialog()
        true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
        (context as RouteActivityController).setOnOpenAudiosDialogClick { openAudiosDialog() }
    }

    override fun setUpViews(view: View) {
        setUpPlayer()
        viewModel.setOnPlayerTimerListener(binding.playerView::setCurrentPosition)
        binding.playerView.setOnPlayerClickListener(viewModel.routePlayer.onPlayerClickListener)

        binding.mapView.createRoute(
            viewModel.routeAudios.map { location ->
                PlaceMark(
                    location.id,
                    location.point,
                    MarkType.NUMBERED,
                    objectTapListener
                )
            }
        )
    }

    private fun setUpPlayer() {
        val currentAudio = viewModel.getCurrentPlayingAudio()
        binding.playerView.isVisible = viewModel.getIsSomeonePlaying()
        binding.playerView.playButton.setUiState(viewModel.getIsSomeonePlaying())
        binding.playerView.setTrackName(currentAudio?.name.toString())
        binding.playerView.setDurationInSeconds(currentAudio?.durationInSeconds ?: 0)
    }

    private fun openAudiosDialog() {
        RouteAudiosDialog().also {
            it.setOnDismiss { setUpPlayer() }
        }.show(parentFragmentManager, "tag")
    }
}