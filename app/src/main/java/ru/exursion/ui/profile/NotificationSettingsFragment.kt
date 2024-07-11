package ru.exursion.ui.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.ui.getColorByAttr
import ru.exursion.R
import ru.exursion.data.models.SwitchSetting
import ru.exursion.databinding.FragmentNotificationSettingsBinding
import ru.exursion.ui.shared.ext.addItemDivider
import ru.exursion.ui.shared.ext.addItemMargins

class NotificationSettingsFragment :
    BaseFragment<FragmentNotificationSettingsBinding>(FragmentNotificationSettingsBinding::class.java) {

    override fun setUpViews(view: View): Unit = with(binding) {
        val switchSettings = listOf(
            SwitchSetting(getString(R.string.screen_settings_notifications_news), true, ::setNewsState)
        )
        val adapter = SettingsAdapter().apply { swapData(switchSettings) }

        header.backButton.setOnClickListener{ findNavController().navigateUp() }
        header.title.text = getString(R.string.screen_profile_notifications)
        settingRecycler.adapter = adapter
        context?.getColorByAttr(R.attr.exc_divider_color)?.let {
            settingRecycler.addItemDivider(it)
        }
        settingRecycler.addItemMargins(0, 10)
    }

    private fun setNewsState(state: Boolean) {

    }
}