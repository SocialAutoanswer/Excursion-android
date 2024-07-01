package ru.exursion.ui.auth.fragments

import android.content.Context
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.ui.getColorByAttr
import ru.bibaboba.kit.ui.getHtmlString
import ru.bibaboba.kit.util.toTimeFormat
import ru.exursion.R
import ru.exursion.databinding.FragmentEnterAuthCodeBinding
import ru.exursion.ui.auth.vm.AuthViewModel
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class EnterCodeFragment : BaseFragment<FragmentEnterAuthCodeBinding>(
    FragmentEnterAuthCodeBinding::class.java
) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<AuthViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) {
        with(binding) {

            header.title.text = context?.getString(R.string.screen_enter_code_title)
            header.backButton.setOnClickListener { findNavController().navigateUp() }

            codeEdit.setCompleteListener { completed -> continueButton.isEnabled = completed }

            continueButton.setOnClickListener {
                viewModel.confirmAuthCode(codeEdit.text)
            }

            codeHint.text = context?.getString(R.string.screen_enter_code_hint, viewModel.email)

            binding.timer.setOnClickListener { _ ->
                viewModel.sendAuthCode()
                binding.codeEdit.text = ""
                viewModel.startTimer()
                binding.timer.setOnClickListener(null)
            }
        }

        setUpTimer()
        viewModel.startTimer()
    }

    private fun setUpTimer() {
        val primaryColor = context?.getColorByAttr(R.attr.exc_color_primary).toString()

        viewModel.setOnTimerTick { currentMilly ->
            binding.timer.text = context?.getHtmlString(
                R.string.screen_enter_code_timer,
                primaryColor,
                currentMilly.toTimeFormat()
            )
        }

        viewModel.setOnTimerFinish {
            binding.timer.text = context?.getHtmlString(
                R.string.screen_enter_code_send_code,
                primaryColor
            )
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopTimer()
    }
}