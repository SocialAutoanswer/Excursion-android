package ru.exursion.ui.auth.fragments

import android.content.Context
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
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

class EnterEmailFragment :
    BaseFragment<FragmentEnterEmailBinding>(FragmentEnterEmailBinding::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AuthViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) {
        with(binding) {
            emailEdit.addTextChangedListener { s: CharSequence?, _: Int, _: Int, _: Int ->
                continueBtn.isEnabled = s?.isValidEmail() ?: false
            }


            continueBtn.setOnClickListener {
                viewModel.sendMessageToEmail(emailEdit.text.toString())

                findNavController().navigate(
                    R.id.enterCodeFragment,
                    bundleOf("user_email" to emailEdit.text.toString())
                )
            }
        }
    }

}