package ru.exursion.ui.routes.fragments

import android.content.Context
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateBottomSheetDialogFragment
import ru.exursion.data.models.Audio
import ru.exursion.data.models.AudioLocation
import ru.exursion.databinding.FragmentRouteLocationsDialogBinding
import ru.exursion.ui.routes.adapter.RouteAudiosDelegateAdapter
import ru.exursion.ui.routes.vm.RouteDetailsViewModel
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject


class RouteAudiosDialog :
    StateBottomSheetDialogFragment<FragmentRouteLocationsDialogBinding, RouteDetailsViewModel>(
        FragmentRouteLocationsDialogBinding::class.java
    ) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<RouteDetailsViewModel> { viewModelFactory }

    private val adapter = CompositeDelegateAdapter(RouteAudiosDelegateAdapter(null))

    override val stateMachine = StateMachine.Builder()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) {
        binding.locationRecycler.also {
            it.adapter = adapter
            it.addItemMargins(25, 16)
        }

        binding.title.text = viewModel.routeName

        adapter.swapData(viewModel.routeAudios)
    }
}