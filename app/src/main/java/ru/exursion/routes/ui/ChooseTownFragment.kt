package ru.exursion.routes.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.databinding.FragmentChooseTownBinding
import ru.exursion.inject
import ru.exursion.routes.TownsDelegateAdapter
import ru.exursion.routes.TownsUseCase
import ru.exursion.routes.vm.ChooseTownViewModel
import javax.inject.Inject

class ChooseTownFragment : BaseFragment<FragmentChooseTownBinding>(FragmentChooseTownBinding::class.java) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ChooseTownViewModel by viewModels { viewModelFactory }

    private val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addLoadingState()
        .addReadyState()
        .build()

    private val adapter = CompositeDelegateAdapter(TownsDelegateAdapter())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews() {
        binding.townsList.adapter = adapter
    }

    override fun getStartData() {
        viewModel.requestTownsIfNeeded()
    }

    override fun setUpObservers() {
        viewModel.state.observe(viewLifecycleOwner, stateMachine::submit)
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            ChooseTownViewModel.ChooseTownState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(ChooseTownViewModel.ChooseTownState.Ready::class) {
            adapter.swapData(it.towns)
        }
    }
}