package ru.exursion.routes.ui

import android.content.Context

import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentChooseTownBinding
import ru.exursion.inject
import ru.exursion.networkErrorDialog
import ru.exursion.routes.TownsDelegateAdapter
import ru.exursion.routes.vm.ChooseTownViewModel
import ru.exursion.shared.ui.dialog.dialog
import javax.inject.Inject

class ChooseTownFragment : BaseFragment<FragmentChooseTownBinding>(FragmentChooseTownBinding::class.java) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ChooseTownViewModel> { viewModelFactory }

    private val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addLoadingState()
        .addReadyState()
        .addErrorEffect()
        .build()

    private val adapter = CompositeDelegateAdapter(TownsDelegateAdapter {
        findNavController().navigate(R.id.fragment_town_routes)
    })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) {
        binding.townsList.adapter = adapter
    }

    override fun getStartData() {
        viewModel.requestTownsIfNeeded()
    }

    override fun setUpObservers() {
        viewModel.state.observe(viewLifecycleOwner, stateMachine::submit)
        viewModel.effect.observe(viewLifecycleOwner, stateMachine::submit)
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            ChooseTownViewModel.ChooseTownState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(ChooseTownViewModel.ChooseTownState.Ready::class) { adapter.swapData(it.towns) }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(ChooseTownViewModel.ChooseTownEffect.Error::class) {
            networkErrorDialog {
                onClick { it?.dismiss() }
                onDismiss {
                    viewModel.requestTowns()
                    Toast.makeText(context, R.string.dialog_network_error_data_requested, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}