package ru.exursion.ui.profile

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentProfileBinding
import ru.exursion.domain.settings.AppSettings
import ru.exursion.domain.settings.UserSettings
import ru.exursion.ui.MainActivity
import ru.exursion.ui.auth.AuthActivity
import ru.exursion.ui.shared.dialog.dialog
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::class.java) {

    @Inject lateinit var userSettings: UserSettings

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) = with(binding) {
        settingsBtn.setOnClickListener{ findNavController().navigate(R.id.redactProfileFragment) }
        favoriteBtn.setOnClickListener { findNavController().navigate(R.id.favoritesFragment) }
        faqBtn.setOnClickListener { findNavController().navigate(R.id.questionsFragment) }
        notificationBtn.setOnClickListener { findNavController().navigate(R.id.notificationSettingsFragment) }
        decorBtn.setOnClickListener { findNavController().navigate(R.id.decorSettingsFragment) }
        feedbackBtn.setOnClickListener { findNavController().navigate(R.id.feedbackFragment) }
        aboutAppBtn.setOnClickListener { findNavController().navigate(R.id.aboutAppFragment) }

        logoutBtn.setOnClickListener{
            dialog("logout") {
                title = getString(R.string.dialog_logout_title)
                confirmButtonText = getString(R.string.dialog_logout_confirm)
                rejectButtonText = getString(R.string.dialog_logout_reject)

                onDismiss { it?.dismiss() }
                onConfirm {
                    val activity = activity ?: return@onConfirm
                    userSettings.clearAllPrefs()
                    it?.dismiss()
                    startActivity(Intent(activity, AuthActivity::class.java))
                }
            }
        }
    }

}