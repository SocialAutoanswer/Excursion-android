package ru.exursion.ui.auth.fragments

import android.content.Context
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.util.isValidEmail
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.util.addTextChangedListener
import ru.bibaboba.kit.util.isValidPassword
import ru.exursion.R
import ru.exursion.databinding.FragmentSignupBinding
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class SignUpFragment : BaseFragment<FragmentSignupBinding>(
    FragmentSignupBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<AuthViewModel> { viewModelFactory }

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
            viewModel.email = emailEdit.text.toString()
            viewModel.signUp()
            findNavController().navigate(R.id.enter_code_fragment)
        }
    }

    private fun FragmentSignupBinding.isFieldsValid() =
        emailEdit.text.isValidEmail()
                && passEdit.text.isValidPassword()

}