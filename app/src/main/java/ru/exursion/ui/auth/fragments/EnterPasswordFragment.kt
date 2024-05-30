package ru.exursion.ui.auth.fragments

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentEnterPasswordBinding
import ru.exursion.ui.auth.vm.AuthViewModel
import javax.inject.Inject

class EnterPasswordFragment : BaseFragment<FragmentEnterPasswordBinding>(
    FragmentEnterPasswordBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by activityViewModels<AuthViewModel> { viewModelFactory }

    override fun setUpViews(view: View) = with(binding) {
        header.title.text = getString(R.string.screen_enter_password_title)
        header.backButton.setOnClickListener { findNavController().navigateUp() }
        continueButton.setOnClickListener {
            viewModel.password = passwordEdit.text.toString()
            findNavController().navigate(R.id.enter_user_data_fragment)
        }

    }
}