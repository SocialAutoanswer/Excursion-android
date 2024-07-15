package ru.exursion.ui.auth.fragments

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.util.isValidEmail
import ru.bibaboba.kit.ui.StateFragment
import ru.bibaboba.kit.util.isValidPassword
import ru.exursion.R
import ru.exursion.databinding.FragmentSignupBinding
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.ui.shared.ext.addTextChangedListener
import ru.exursion.ui.shared.ext.inject
import ru.exursion.ui.shared.ext.networkErrorDialog
import ru.exursion.ui.shared.ext.setErrorState
import javax.inject.Inject

class SignUpFragment : StateFragment<FragmentSignupBinding, AuthViewModel>(
    FragmentSignupBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by activityViewModels<AuthViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addSuccessState()
        .addErrorEffect()
        .addNetworkErrorEffect()
        .addEmailErrorEffect()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) = with(binding) {
        header.title.text = getString(R.string.screen_choose_auth_method_signup_button)
        header.backButton.setOnClickListener { findNavController().navigateUp() }

        emailEdit.addTextChangedListener { _, _, _, _ ->
            continueButton.isEnabled = isFieldsValid()
        }

        passEdit.addTextChangedListener { _, _, _, _ ->
            continueButton.isEnabled = isFieldsValid()
        }

        continueButton.setOnClickListener {
            viewModel.user.email = emailEdit.text.toString()
            viewModel.user.password = passEdit.text.toString()
            viewModel.signUp()
        }
    }

    private fun FragmentSignupBinding.isFieldsValid() =
        emailEdit.text.isValidEmail()
                && passEdit.text.isValidPassword()

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            AuthViewModel.AuthState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addSuccessState(): StateMachine.Builder {
        return addState(AuthViewModel.AuthState.Success::class) {
            findNavController().navigate(R.id.enter_code_fragment)
        }
    }

    private fun StateMachine.Builder.addNetworkErrorEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.NetworkError::class) {
            networkErrorDialog {
                onClick { it?.dismiss() }
                onDismiss {
                    Toast.makeText(context, R.string.dialog_network_error_data_requested, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun StateMachine.Builder.addEmailErrorEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.EmailAlreadyRegistered::class) {
            binding.emailEdit.setErrorState()
            Toast.makeText(context, R.string.screen_signup_email_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}