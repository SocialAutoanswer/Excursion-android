package ru.exursion.ui.auth.fragments

import android.view.View
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.ui.utils.SimpleTextWatcher
import ru.bibaboba.kit.util.NumberEditTextFilter
import ru.exursion.R
import ru.exursion.databinding.FragmentEnterUserDataBinding

class EnterUserDataFragment : BaseFragment<FragmentEnterUserDataBinding>(
    FragmentEnterUserDataBinding::class.java
) {

    override fun setUpViews(view: View) {

        with(binding) {
            header.title.text = getString(R.string.screen_enter_user_data_title)
            header.backButton.setOnClickListener { findNavController().navigateUp() }

            nameInput.setUp()
            lastNameInput.setUp()
            dateDayInput.setUp(2, NumberEditTextFilter.Day)
            dateMonthInput.setUp(2, NumberEditTextFilter.Month)
            dateYearInput.setUp(4, NumberEditTextFilter.Year)

            continueButton.setOnClickListener {
                findNavController().navigate(R.id.signup_fragment)
            }
        }
    }


    private fun EditText.setUp(maxLength: Int? = null, filter: NumberEditTextFilter? = null) {
        addTextChangedListener(SimpleTextWatcher { s, _, _, _ ->
            binding.continueButton.isEnabled = binding.isAllInputsFilled()

            if (maxLength == s.length) {
                focusSearch(View.FOCUS_RIGHT)?.requestFocus()
            }
        })

        filter?.let { filters = arrayOf(it) }
    }

    private fun FragmentEnterUserDataBinding.isAllInputsFilled(): Boolean =
        nameInput.text.isNotBlank()
                && lastNameInput.text.isNotBlank()
                && dateDayInput.text.isNotBlank()
                && dateMonthInput.text.isNotBlank()
                && dateYearInput.text.length == 4

}