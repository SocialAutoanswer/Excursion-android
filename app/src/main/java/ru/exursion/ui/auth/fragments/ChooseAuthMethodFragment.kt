package ru.exursion.ui.auth.fragments

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentChooseAuthMethodBinding

class ChooseAuthMethodFragment: BaseFragment<FragmentChooseAuthMethodBinding>(
    FragmentChooseAuthMethodBinding::class.java
) {
    override fun setUpViews(view: View) = with(binding) {
        signupButton.setOnClickListener { findNavController().navigate(R.id.enter_user_data_fragment) }
        signinButton.setOnClickListener { findNavController().navigate(R.id.signin_fragment) }
    }

}