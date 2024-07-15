package ru.exursion.ui.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentFeedbackBinding
import ru.exursion.domain.AppNavigator

class FeedbackFragment: BaseFragment<FragmentFeedbackBinding>(FragmentFeedbackBinding::class.java) {

    override fun setUpViews(view: View) = with(binding) {
        header.title.text = getString(R.string.screen_profile_feedback)
        header.backButton.setOnClickListener{ findNavController().navigateUp() }
        telegramBtn.setOnClickListener{ AppNavigator.Telegram().navigate(::startActivity) }
        whatsappBtn.setOnClickListener{ AppNavigator.WhatsApp().navigate(::startActivity) }
    }

}