package ru.exursion.ui.profile

import android.content.Intent
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentRedactProfileBinding
import ru.exursion.ui.auth.AuthActivity
import ru.exursion.ui.shared.dialog.dialog

class RedactProfileFragment :
    BaseFragment<FragmentRedactProfileBinding>(FragmentRedactProfileBinding::class.java) {

    override fun setUpViews(view: View) = with(binding) {
        backButton.setOnClickListener{ findNavController().navigateUp() }

        removeProfile.setOnClickListener{
            dialog("delete_profile") {
                title = getString(R.string.dialog_delete_profile_title)
                summary = getString(R.string.dialog_delete_profile_summary)
                confirmButtonText = getString(R.string.dialog_delete_profile_confirm)
                rejectButtonText = getString(R.string.dialog_delete_profile_reject)

                onConfirm {
                    val activity = activity ?: return@onConfirm
                    //viewModel.deleteProfile()
                    it?.dismiss()
                    startActivity(Intent(activity, AuthActivity::class.java))
                }

                onDismiss { it?.dismiss() }
            }
        }
    }

}