package ru.exursion.routes.ui

import android.content.Context
import android.location.Location

import android.view.View
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.shared.ext.addItemMargins
import ru.exursion.databinding.FragmentChooseTownBinding
import ru.exursion.shared.ext.inject
import ru.exursion.shared.ext.networkErrorDialog
import ru.exursion.routes.TownsDelegateAdapter
import ru.exursion.routes.vm.ChooseCityViewModel
import javax.inject.Inject

class ChooseCityFragment : StateFragment<FragmentChooseTownBinding, ChooseCityViewModel>(FragmentChooseTownBinding::class.java) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<ChooseCityViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
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
        Location
        binding.townsList.also {
            it.adapter = adapter
            it.addItemMargins(vertical = 16)
        }
    }

    override fun getStartData() = viewModel.requestTownsIfNeeded()

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            ChooseCityViewModel.ChooseCityState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(ChooseCityViewModel.ChooseCityState.Ready::class) { adapter.swapData(it.towns) }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(ChooseCityViewModel.ChooseCityEffect.Error::class) {
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