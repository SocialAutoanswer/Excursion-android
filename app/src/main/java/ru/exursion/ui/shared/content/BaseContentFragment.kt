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
import androidx.recyclerview.widget.RecyclerView
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentContentExcBinding
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

abstract class BaseContentFragment :
    StateFragment<FragmentContentExcBinding, BaseContentViewModel>(FragmentContentExcBinding::class.java) {

    companion object {
        const val CITY_ID_BUNDLE_KEY = "city_id"
        const val TAG_NAME_BUNDLE_KEY = "tag_name_id"
        const val TAG_ID_BUNDLE_KEY = "tag_id"
        const val IS_FAVORITE_BUNDLE_KEY = "is_favorite"
    }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addReadyState()
        .addIdleState()
        .addErrorEffect()
        .build()

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<BaseContentViewModel> { viewModelFactory }

    abstract val adapter: RecyclerView.Adapter<*>

    @get:StringRes
    abstract val  titleResId: Int?

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    open fun readyCallback() {}

    abstract fun getData()

    @CallSuper
    override fun getStartData() {
        viewModel.isFavorite = arguments?.getBoolean(IS_FAVORITE_BUNDLE_KEY, false) ?: false
        viewModel.cityId = arguments?.getLong(CITY_ID_BUNDLE_KEY, -1) ?: -1
        viewModel.tagId = arguments?.getLong(TAG_ID_BUNDLE_KEY, -1) ?: -1
        getData()
    }

    @CallSuper
    override fun setUpViews(view: View): Unit = with(binding) {
        recycler.also {
            it.adapter = adapter
            it.addItemMargins(25, 16)
        }

        header.title.text = titleResId?.let { getString(it) }
        header.backButton.setOnClickListener{ findNavController().navigateUp() }
        refreshLayout.setOnRefreshListener { getData() }
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            BaseContentViewModel.ContentState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = {
                binding.refreshLayout.isRefreshing = false
                binding.loading.root.isVisible = false
            }
        )
    }

    private fun StateMachine.Builder.addReadyState(): StateMachine.Builder {
        return addState(BaseContentViewModel.ContentState.Ready::class) {
            readyCallback()
            (adapter as CompositeDelegateAdapter).swapData(it.content)
        }
    }

    private fun StateMachine.Builder.addIdleState() : StateMachine.Builder {
        return addState(
            BaseContentViewModel.ContentState.Idle::class,
            callback = { binding.emptyLayout.root.isVisible = true },
            onExit = { binding.emptyLayout.root.isVisible = false }
        )
    }

    private fun StateMachine.Builder.addErrorEffect() : StateMachine.Builder {
        return addEffect(BaseContentViewModel.ContentEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}
