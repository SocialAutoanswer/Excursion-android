package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateBottomSheetDialogFragment
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
    override val viewModel by activityViewModels<RouteDetailsViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) {
        val adapter = CompositeDelegateAdapter(RouteAudiosDelegateAdapter(viewModel.routePlayer))

        binding.locationRecycler.also {
            it.adapter = adapter
            it.addItemMargins(25, 16)
        }

        binding.title.text = viewModel.routeName

        adapter.swapData(viewModel.routeAudios)
    }
}