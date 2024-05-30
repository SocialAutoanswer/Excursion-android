package ru.exursion.ui.auth.fragments

import android.content.Intent
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentEnterUserDataBinding
import ru.exursion.ui.MainActivity

class EnterUserDataFragment : BaseFragment<FragmentEnterUserDataBinding>(
    FragmentEnterUserDataBinding::class.java
) {

    override fun setUpViews(view: View) {
        with(binding) {
            header.title.text = getString(R.string.screen_enter_user_data_title)
            header.backButton.setOnClickListener { findNavController().navigateUp() }
            continueButton.setOnClickListener {
                if (!binding.isAllInputsFilled()) {
                    // TODO: implement errors showing
                    return@setOnClickListener
                }
                val activity = activity ?: return@setOnClickListener
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    private fun FragmentEnterUserDataBinding.isAllInputsFilled(): Boolean {
        return nameInput.text.isNotBlank()
                && lastNameInput.text.isNotBlank()
                && dateDayInput.text.isNotBlank()
                && dateMonthInput.text.isNotBlank()
                && dateYearInput.text.isNotBlank()
    }
}