package ru.exursion.ui.events

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentContentExcBinding
import ru.exursion.ui.shared.FooterLoadStateAdapter
import ru.exursion.ui.shared.content.BaseContentFragment
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class ShopsFragment : StateFragment<FragmentContentExcBinding, ShopsViewModel>(FragmentContentExcBinding::class.java) {

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addReadyState()
        .addIdleState()
        .addErrorEffect()
        .build()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<ShopsViewModel> { viewModelFactory }

    private val adapter = CompositeDelegateAdapter(
        ShopsDelegateAdapter {
            findNavController().navigate(
                R.id.shopDetailsFragment,
                bundleOf(ShopDetailsFragment.SHOP_BUNDLE_KEY to it)
            )
        }
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun getStartData() {
        val tagId = arguments?.getLong(BaseContentFragment.TAG_ID_BUNDLE_KEY) ?: return

        viewModel.getShops(tagId)
    }

    override fun setUpViews(view: View) {
        val name = arguments?.getString(BaseContentFragment.TAG_NAME_BUNDLE_KEY) ?: ""

        binding.header.title.text = name
        binding.header.backButton.setOnClickListener { activity?.onBackPressed() }

        binding.recycler.also {
            it.adapter = adapter
            it.addItemMargins(25, 16)
        }
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            ShopsViewModel.ContentState.Loading::class,
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
        return addState(ShopsViewModel.ContentState.Ready::class) {
            adapter.swapData(it.content)
        }
    }

    private fun StateMachine.Builder.addIdleState() : StateMachine.Builder {
        return addState(
            ShopsViewModel.ContentState.Idle::class,
            callback = { binding.emptyLayout.root.isVisible = true },
            onExit = { binding.emptyLayout.root.isVisible = false }
        )
    }

    private fun StateMachine.Builder.addErrorEffect() : StateMachine.Builder {
        return addEffect(ShopsViewModel.ContentEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}