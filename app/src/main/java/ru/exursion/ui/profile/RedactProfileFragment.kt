package ru.exursion.ui.profile

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.data.models.User
import ru.exursion.databinding.FragmentRedactProfileBinding
import ru.exursion.ui.auth.AuthActivity
import ru.exursion.ui.shared.dialog.dialog
import ru.exursion.ui.shared.ext.inject
import ru.exursion.ui.shared.ext.networkErrorDialog
import javax.inject.Inject

class RedactProfileFragment :
    StateFragment<FragmentRedactProfileBinding, ProfileViewModel>(FragmentRedactProfileBinding::class.java) {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by viewModels<ProfileViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addProfileReceivedState()
        .addProfileEditedState()
        .addProfileDeletedState()
        .addErrorEffect()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
        viewModel.getProfile()
    }

    override fun setUpViews(view: View) = with(binding) {
        backButton.setOnClickListener{ findNavController().navigateUp() }

        saveButton.setOnClickListener{
            viewModel.editProfile(
                User(
                    firstName = binding.nameInput.text.toString(),
                    lastName = binding.lastNameInput.text.toString(),
                    email = binding.emailInput.text.toString()
                )
            )
        }

        removeProfile.setOnClickListener{
            dialog("delete_profile") {
                title = getString(R.string.dialog_delete_profile_title)
                summary = getString(R.string.dialog_delete_profile_summary)
                confirmButtonText = getString(R.string.dialog_delete_profile_confirm)
                rejectButtonText = getString(R.string.dialog_delete_profile_reject)

                onConfirm {
                    viewModel.deleteProfile()
                    it?.dismiss()
                }
                onDismiss { it?.dismiss() }
            }
        }
    }

    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(ProfileViewModel.ProfileState.Loading::class,
            callback = {
                binding.loading.root.isVisible = true
                binding.root.isEnabled = false
            },
            onExit = {
                binding.loading.root.isVisible = false
                binding.root.isEnabled = true
            },
        )
    }

    private fun StateMachine.Builder.addProfileReceivedState(): StateMachine.Builder {
        return addState(ProfileViewModel.ProfileState.ProfileReceived::class) {
            with(binding) {
                nameInput.setText(it.user?.firstName)
                lastNameInput.setText(it.user?.lastName)
                emailInput.setText(it.user?.email)
            }
        }
    }

    private fun StateMachine.Builder.addProfileEditedState(): StateMachine.Builder {
        return addState(ProfileViewModel.ProfileState.ProfileEdited::class) {
            Toast.makeText(context, R.string.screen_redact_profile_edit_success, Toast.LENGTH_SHORT).show()
        }
    }

    private fun StateMachine.Builder.addProfileDeletedState(): StateMachine.Builder {
        return addState(ProfileViewModel.ProfileState.ProfileDeleted::class) {
            val activity = activity ?: return@addState
            startActivity(Intent(activity, AuthActivity::class.java))
            activity.finish()
        }
    }

    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(ProfileViewModel.ProfileEffect.Error::class) {
            networkErrorDialog {
                onNeutralClick { it?.dismiss() }
                onDismiss { it?.dismiss() }
            }
        }
    }



}