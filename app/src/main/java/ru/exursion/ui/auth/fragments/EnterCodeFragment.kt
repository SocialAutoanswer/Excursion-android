package ru.exursion.ui.auth.fragments

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.bibaboba.kit.ui.getColorByAttr
import ru.bibaboba.kit.ui.getHtmlString
import ru.bibaboba.kit.util.toTimeFormat
import ru.exursion.R
import ru.exursion.databinding.FragmentEnterAuthCodeBinding
import ru.exursion.ui.MainActivity
import ru.exursion.ui.auth.AuthActivity
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.ui.shared.ext.inject
import ru.exursion.ui.shared.ext.networkErrorDialog
import javax.inject.Inject

class EnterCodeFragment : StateFragment<FragmentEnterAuthCodeBinding, AuthViewModel>(
    FragmentEnterAuthCodeBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<AuthViewModel> { viewModelFactory }
    
    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addSuccessState()
        .addErrorEffect()
        .addNetworkErrorEffect()
        .addIncorrectCodeEffect()
        .build()
    
    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) = with(binding) {
        header.title.text = context?.getString(R.string.screen_enter_code_title)
        header.backButton.setOnClickListener { findNavController().navigateUp() }

        codeEdit.setCompleteListener { completed -> continueButton.isEnabled = completed }

        continueButton.setOnClickListener {
            viewModel.confirmAuthCode(codeEdit.text)
        }

        codeHint.text = context?.getString(R.string.screen_enter_code_hint, viewModel.userEmail)

        timer.setOnClickListener { _ ->
            codeEdit.text = ""
            timer.setOnClickListener(null)
            viewModel.sendAuthCode()
            viewModel.startTimer()
        }

        val primaryColor = context?.getColorByAttr(R.attr.exc_color_primary).toString()

        setUpTimer(primaryColor)

        if (arguments?.getBoolean(AuthActivity.IS_START_SCREEN) == true) {
            header.backButton.isVisible = false
            setUpTimerFinished(primaryColor)
        } else {
            viewModel.startTimer()
        }
    }

    private fun setUpTimer(color: String) {
        viewModel.setOnTimerTick { currentMilly ->
            binding.timer.text = context?.getHtmlString(
                R.string.screen_enter_code_timer,
                color,
                currentMilly.toTimeFormat()
            )
        }

        viewModel.setOnTimerFinish { setUpTimerFinished(color) }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopTimer()
    }

    private fun setUpTimerFinished(color: String) {
        binding.timer.text = context?.getHtmlString(R.string.screen_enter_code_send_code, color)
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            AuthViewModel.AuthState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addSuccessState(): StateMachine.Builder {
        return addState(AuthViewModel.AuthState.Success::class) {
            val activity = activity ?: return@addState
            startActivity(Intent(activity, MainActivity::class.java))
        }
    }


    private fun StateMachine.Builder.addNetworkErrorEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.NetworkError::class) {
            networkErrorDialog {
                onNeutralClick { it?.dismiss() }
                onDismiss {
                    Toast.makeText(context, R.string.dialog_network_error_data_requested, Toast.LENGTH_SHORT).show()
                    viewModel.sendAuthCode()
                }
            }
        }
    }

    private fun StateMachine.Builder.addIncorrectCodeEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.IncorrectCode::class) {
            //binding.codeEdit.setErrorState or smth
            Toast.makeText(context, R.string.screen_enter_code_incorrect_code, Toast.LENGTH_LONG).show()
        }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}