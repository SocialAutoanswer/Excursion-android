package ru.exursion.ui.shared.content

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentContentExcBinding
import ru.exursion.ui.shared.FooterLoadStateAdapter
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

abstract class BaseContentFragment :
    StateFragment<FragmentContentExcBinding, BaseContentViewModel>(FragmentContentExcBinding::class.java) {

    companion object {
        const val CITY_ID_BUNDLE_KEY = "city_id"
        const val TAG_NAME_BUNDLE_KEY = "tag_name_id"
        const val ROUTE_ID_BUNDLE_KEY = "route_id"
        const val TAG_ID_BUNDLE_KEY = "tag_id"
        const val IS_FAVORITE_BUNDLE_KEY = "is_favorite"
    }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addReadyState()
        .addIdleState()
        .addContentClearedState()
        .addErrorEffect()
        .build()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<BaseContentViewModel> { viewModelFactory }

    abstract val adapter: PagingDataAdapter<*, *>

    @get:StringRes
    abstract val titleResId: Int

    open var isBackButtonVisible = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    open fun readyCallback() {}

    open fun onClearCLick() {}

    abstract fun getData()

    @CallSuper
    override fun getStartData() {
        viewModel.isFavorite = arguments?.getBoolean(IS_FAVORITE_BUNDLE_KEY, false) ?: false
        viewModel.cityId = arguments?.getLong(CITY_ID_BUNDLE_KEY, -1) ?: -1
        viewModel.tagId = arguments?.getLong(TAG_ID_BUNDLE_KEY, -1) ?: -1
        viewModel.tagName = arguments?.getString(TAG_NAME_BUNDLE_KEY, getString(titleResId)) ?: getString(titleResId)
        viewModel.routeId = arguments?.getLong(ROUTE_ID_BUNDLE_KEY, -1) ?: -1
        getData()
    }

    @CallSuper
    override fun setUpViews(view: View): Unit = with(binding) {
        recycler.also {
            it.adapter = adapter.withLoadStateFooter(FooterLoadStateAdapter())
            it.addItemMargins(25, 16)
        }

        adapter.addLoadStateListener { loadState ->
            if(loadState.prepend.endOfPaginationReached) {
                if (adapter.itemCount == 0) {
                    viewModel.setIdleState()
                }
            }
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.source.refresh as? LoadState.Error

            errorState?.let { viewModel.setIdleState() }

            refreshLayout.isRefreshing = loadState.source.refresh is LoadState.Loading
            loading.root.isVisible = loadState.source.refresh is LoadState.Loading
        }

        header.backButton.isVisible = isBackButtonVisible
        header.title.text = viewModel.tagName
        header.backButton.setOnClickListener{ findNavController().navigateUp() }
        refreshLayout.setOnRefreshListener { getData() }
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            BaseContentViewModel.ContentState.Loading::class,
            callback = {
                binding.loading.root.isVisible = true
                binding.root.isEnabled = false
           },
            onExit = {
                binding.loading.root.isVisible = false
                binding.refreshLayout.isRefreshing = false
                binding.root.isEnabled = false
            }
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(BaseContentViewModel.ContentState.Ready::class) {
            readyCallback()

            if (viewModel.isFavorite) {
                binding.clearButton.isVisible = true

                binding.clearButton.setOnClickListener {
                    onClearCLick()
                }
            }

            (adapter as PagingDataAdapter<Any, *>).submitData(lifecycle, it.content)
        }
    }

    private fun StateMachine.Builder.addIdleState() : StateMachine.Builder {
        return addState(
            BaseContentViewModel.ContentState.Idle::class,
            callback = { binding.emptyLayout.root.isVisible = true },
            onExit = { binding.emptyLayout.root.isVisible = false }
        )
    }

    private fun StateMachine.Builder.addContentClearedState(): StateMachine.Builder {
        return addState(BaseContentViewModel.ContentState.ContentCleared::class) {
            adapter.notifyItemRangeRemoved(0, adapter.itemCount)
            viewModel.setIdleState()
        }
    }

    private fun StateMachine.Builder.addErrorEffect() : StateMachine.Builder {
        return addEffect(BaseContentViewModel.ContentEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}
