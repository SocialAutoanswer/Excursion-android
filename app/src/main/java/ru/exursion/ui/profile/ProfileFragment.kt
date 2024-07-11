package ru.exursion.ui.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentProfileBinding

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::class.java) {

    override fun setUpViews(view: View) = with(binding) {
        settingsBtn.setOnClickListener{ findNavController().navigate(R.id.redactProfileFragment) }
        favoriteBtn.setOnClickListener { findNavController().navigate(R.id.favoritesFragment) }
        faqBtn.setOnClickListener { findNavController().navigate(R.id.questionsFragment) }
        notificationBtn.setOnClickListener { findNavController().navigate(R.id.notificationSettingsFragment) }
        decorBtn.setOnClickListener { findNavController().navigate(R.id.decorSettingsFragment) }
        feedbackBtn.setOnClickListener { findNavController().navigate(R.id.feedbackFragment) }
        aboutAppBtn.setOnClickListener { findNavController().navigate(R.id.aboutAppFragment) }
    }

}