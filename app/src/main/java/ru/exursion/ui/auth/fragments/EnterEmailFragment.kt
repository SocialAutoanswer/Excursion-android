package ru.exursion.ui.auth.fragments

import android.content.Context
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.util.isValidEmail
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.util.addTextChangedListener
import ru.exursion.R
import ru.exursion.databinding.FragmentEnterEmailBinding
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class EnterEmailFragment : BaseFragment<FragmentEnterEmailBinding>(
    FragmentEnterEmailBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<AuthViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) {
        with(binding) {
            emailEdit.addTextChangedListener { string, _, _, _ ->
                continueButton.isEnabled = string?.isValidEmail() ?: false
            }

            continueButton.setOnClickListener {
                viewModel.email = emailEdit.text.toString()
                findNavController().navigate(R.id.enter_password_fragment)
            }
        }
    }

}