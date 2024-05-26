package ru.exursion.ui.routes

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.databinding.FragmentTownRouteTypesBinding
import ru.exursion.ui.shared.ext.inject
import ru.exursion.ui.routes.vm.TownRouteTypesViewModel
import ru.exursion.ui.shared.SelectItemDelegateAdapter
import javax.inject.Inject

class TownRouteTypesFragment : StateFragment<FragmentTownRouteTypesBinding, TownRouteTypesViewModel>(
    FragmentTownRouteTypesBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<TownRouteTypesViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addLoadingState()
        .addReadyState()
        .build()

    private val adapter = CompositeDelegateAdapter(SelectItemDelegateAdapter(true) {
        //findNavController().navigate()
    })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun getStartData() {
        viewModel.requestTownRouteTypes()
    }

    override fun setUpViews(view: View): Unit = with(binding) {
        recycler.also {
            it.adapter = adapter
            it.addItemMargins(vertical = 16)
        }
        header.title.setText(R.string.screen_town_routes_title)
        header.backButton.setOnClickListener { activity?.onBackPressed() }
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            TownRouteTypesViewModel.RoutesState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false }
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(TownRouteTypesViewModel.RoutesState.Ready::class) { adapter.swapData(it.routeTypes) }
    }
}