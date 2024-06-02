package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.databinding.FragmentRoutesBinding
import ru.exursion.ui.routes.RoutesPagingDataAdapter
import ru.exursion.ui.routes.vm.RoutesViewModel
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class RoutesFragment : StateFragment<FragmentRoutesBinding, RoutesViewModel>(
    FragmentRoutesBinding::class.java
) {

    companion object {
        const val CITY_ID_BUNDLE_KEY = "city_id"
        const val TAG_NAME_BUNDLE_KEY = "tag_name_id"
        const val TAG_ID_BUNDLE_KEY = "tag_id"
    }

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<RoutesViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addLoadingState()
        .addReadyState()
        .build()

    private val adapter = RoutesPagingDataAdapter {

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) {
        val title = arguments?.getString(TAG_NAME_BUNDLE_KEY) ?: return

        with(binding) {
            header.title.text = title
            header.backButton.setOnClickListener { activity?.onBackPressed() }
            recycler.adapter = adapter
        }
    }

    override fun getStartData() {
        val cityId = arguments?.getLong(CITY_ID_BUNDLE_KEY, -1) ?: return
        val tagId = arguments?.getInt(TAG_ID_BUNDLE_KEY, -1) ?: return
        viewModel.getRoutesByCityId(cityId, tagId)
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            RoutesViewModel.RoutesState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(RoutesViewModel.RoutesState.Ready::class) {
            adapter.submitData(lifecycle, it.routes)
        }
    }
}