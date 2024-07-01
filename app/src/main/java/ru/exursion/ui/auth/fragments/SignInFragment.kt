package ru.exursion.ui.auth.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.ui.utils.SimpleTextWatcher
import ru.bibaboba.kit.util.isValidEmail
import ru.bibaboba.kit.util.isValidPassword
import ru.exursion.R
import ru.exursion.databinding.FragmentSigninBinding
import ru.exursion.ui.auth.vm.AuthViewModel
import javax.inject.Inject

class SignInFragment : BaseFragment<FragmentSigninBinding>(
    FragmentSigninBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<AuthViewModel> { viewModelFactory }

    override fun setUpViews(view: View) = with(binding) {
        header.title.text = getString(R.string.screen_choose_auth_method_signin_button)
        header.backButton.setOnClickListener { findNavController().navigateUp() }

        val enableButtonListener = SimpleTextWatcher { _, _, _, _ ->
            continueButton.isEnabled = isFieldsValid()
        }

        emailEdit.addTextChangedListener(enableButtonListener)
        passEdit.addTextChangedListener(enableButtonListener)

        continueButton.setOnClickListener {
            viewModel.signIn()
        }
    }

    private fun FragmentSigninBinding.isFieldsValid() =
        emailEdit.text.isValidEmail()
                && passEdit.text.isValidPassword()
}