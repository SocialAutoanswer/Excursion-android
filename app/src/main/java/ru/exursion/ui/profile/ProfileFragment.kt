package ru.exursion.ui.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentProfileBinding
import ru.exursion.domain.settings.UserSettings
import ru.exursion.ui.auth.AuthActivity
import ru.exursion.ui.shared.dialog.dialog
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class ProfileFragment :
    StateFragment<FragmentProfileBinding, ProfileViewModel>(FragmentProfileBinding::class.java) {

    @Inject lateinit var userSettings: UserSettings
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel by viewModels<ProfileViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .addProfileReceivedState()
        .addErrorEffect()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
        viewModel.getProfile()
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
                    activity.finish()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun StateMachine.Builder.addProfileReceivedState(): StateMachine.Builder {
        return addState(ProfileViewModel.ProfileState.ProfileReceived::class) {
            binding.userName.text = "${it.user?.firstName} ${it.user?.lastName}"
        }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(ProfileViewModel.ProfileEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_SHORT).show()
        }
    }
}