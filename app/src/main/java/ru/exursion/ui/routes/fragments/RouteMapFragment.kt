package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.map.MapObjectTapListener
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.databinding.FragmentRouteMapBinding
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
        RouteAudiosDialog().show(parentFragmentManager, "")
        true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) {
        binding.playerView.isVisible = viewModel.getIsSomeonePlaying()

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
}