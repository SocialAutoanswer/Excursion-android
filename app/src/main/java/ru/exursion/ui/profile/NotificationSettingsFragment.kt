package ru.exursion.ui.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.data.models.SwitchSetting
import ru.exursion.databinding.FragmentNotificationSettingsBinding

class NotificationSettingsFragment :
    BaseFragment<FragmentNotificationSettingsBinding>(FragmentNotificationSettingsBinding::class.java) {

    override fun setUpViews(view: View) = with(binding) {
        val switchSettings = listOf(
            SwitchSetting(getString(R.string.screen_settings_notifications_news), true, ::setNewsState)
        )
        val adapter = SettingsAdapter().apply { swapData(switchSettings) }

        header.backButton.setOnClickListener{ findNavController().navigateUp() }
        header.title.text = getString(R.string.screen_profile_notifications)
        settingRecycler.adapter = adapter
    }

    private fun setNewsState(state: Boolean) {

    }
}