package ru.exursion.ui.auth.fragments

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentEnterPasswordBinding

class EnterPasswordFragment : BaseFragment<FragmentEnterPasswordBinding>(
    FragmentEnterPasswordBinding::class.java
){

    override fun setUpViews(view: View) {
        binding.header.title.text = getString(R.string.screen_enter_password_title)
        binding.header.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.continueButton.setOnClickListener {
            findNavController().navigate(R.id.enter_user_data_fragment)
        }

    }
}