package ru.exursion.ui.routes.fragments

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.databinding.FragmentRoutesBinding
import ru.exursion.ui.routes.vm.RoutesViewModel
import javax.inject.Inject

class RoutesFragment : StateFragment<FragmentRoutesBinding, RoutesViewModel>(
    FragmentRoutesBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<RoutesViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .build()


}