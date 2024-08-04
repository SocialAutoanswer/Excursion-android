package ru.exursion.ui.profile

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentQuestionsBinding
import ru.exursion.ui.shared.FooterLoadStateAdapter
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class QuestionsFragment: StateFragment<FragmentQuestionsBinding, ProfileViewModel>(
    FragmentQuestionsBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<ProfileViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addQuestionsReceivedState()
        .addErrorEffect()
        .build()

    private val adapter = DropDownQuestionAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
        viewModel.getQuestions()
    }

    override fun setUpViews(view: View): Unit = with(binding) {
        header.title.text = getString(R.string.screen_profile_faq)
        header.backButton.setOnClickListener{ findNavController().navigateUp() }
        questionRecycler.also {
            it.adapter = adapter.withLoadStateFooter(FooterLoadStateAdapter())
            it.addItemMargins(0, 16)
        }

        adapter.addLoadStateListener { loadState ->
            if(loadState.prepend.endOfPaginationReached) {
                binding.emptyLayout.root.isVisible = adapter.itemCount == 0
            }
        }

        refreshLayout.setOnRefreshListener { viewModel.getQuestions() }
    }


    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            ProfileViewModel.ProfileState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = {
                binding.refreshLayout.isRefreshing = false
                binding.loading.root.isVisible = false
             }
        )
    }

    private fun StateMachine.Builder.addQuestionsReceivedState(): StateMachine.Builder {
        return addState(ProfileViewModel.ProfileState.QuestionsReceived::class) {
            adapter.submitData(lifecycle, it.questions)
        }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(ProfileViewModel.ProfileEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}