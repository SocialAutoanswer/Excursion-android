package ru.exursion.ui.routes.fragments

import android.content.Context
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentTagsBinding
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.inject
import ru.exursion.ui.routes.vm.ChooseTagsViewModel
import ru.exursion.ui.shared.TagsPagingAdapter
import javax.inject.Inject

class TagsFragment : StateFragment<FragmentTagsBinding, ChooseTagsViewModel>(
    FragmentTagsBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<ChooseTagsViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addLoadingState()
        .addReadyState()
        .build()

    private val adapter = TagsPagingAdapter {
        findNavController().navigate(
            R.id.fragment_routes,
            bundleOf(
                RoutesFragment.CITY_ID_BUNDLE_KEY to arguments?.getLong(RoutesFragment.CITY_ID_BUNDLE_KEY),
                RoutesFragment.TAG_ID_BUNDLE_KEY to it.id,
                RoutesFragment.TAG_NAME_BUNDLE_KEY to it.name,
            )
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun getStartData() {
        viewModel.requestTags()
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
            ChooseTagsViewModel.ChooseTagsState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false }
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(ChooseTagsViewModel.ChooseTagsState.TagsReady::class) { adapter.submitData(lifecycle, it.tags) }
    }
}