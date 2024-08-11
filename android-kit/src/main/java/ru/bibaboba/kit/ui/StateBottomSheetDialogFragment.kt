package ru.bibaboba.kit.ui

import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.StateMachine

abstract class StateBottomSheetDialogFragment<VB: ViewBinding, VM: RxStateViewModel<*, *>>(bindingClass: Class<VB>) : BaseBottomSheetDialogFragment<VB>(bindingClass) {

    protected abstract val stateMachine: StateMachine

    protected abstract val viewModel: VM

    @CallSuper
    override fun setUpObservers() {
        viewModel.state.observe(viewLifecycleOwner, stateMachine::submit)
        viewModel.effect.observe(viewLifecycleOwner, stateMachine::submit)
    }
}