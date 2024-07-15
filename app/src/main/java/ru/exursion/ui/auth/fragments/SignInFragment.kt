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
import ru.bibaboba.kit.ui.utils.SimpleTextWatcher
import ru.bibaboba.kit.util.isValidEmail
import ru.bibaboba.kit.util.isValidPassword
import ru.exursion.R
import ru.exursion.databinding.FragmentSigninBinding
import ru.exursion.ui.MainActivity
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.ui.shared.ext.inject
import ru.exursion.ui.shared.ext.networkErrorDialog
import ru.exursion.ui.shared.ext.setErrorState
import javax.inject.Inject

class SignInFragment : StateFragment<FragmentSigninBinding, AuthViewModel>(
    FragmentSigninBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<AuthViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addSuccessState()
        .addErrorEffect()
        .addNetworkErrorEffect()
        .addIncorrectPasswordEffect()
        .addEmailNotRegisteredEffect()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) = with(binding) {
        header.title.text = getString(R.string.screen_choose_auth_method_signin_button)
        header.backButton.setOnClickListener { findNavController().navigateUp() }

        val enableButtonListener = SimpleTextWatcher { _, _, _, _ ->
            continueButton.isEnabled = isFieldsValid()
        }

        emailEdit.addTextChangedListener(enableButtonListener)
        passEdit.addTextChangedListener(enableButtonListener)

        continueButton.setOnClickListener {
            viewModel.user.email = emailEdit.text.toString()
            viewModel.user.password = passEdit.text.toString()
            viewModel.signIn()
        }
    }

    private fun FragmentSigninBinding.isFieldsValid() =
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
            val activity = activity ?: return@addState
            startActivity(Intent(activity, MainActivity::class.java))
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

    private fun StateMachine.Builder.addIncorrectPasswordEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.IncorrectPassword::class) {
            binding.passEdit.setErrorState()
            Toast.makeText(context, R.string.screen_signin_error_password, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addEmailNotRegisteredEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.EmailNotRegistered::class) {
            binding.emailEdit.setErrorState()
            Toast.makeText(context, R.string.screen_signin_error_email, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(AuthViewModel.AuthEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}