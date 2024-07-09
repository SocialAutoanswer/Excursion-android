package ru.exursion.ui.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.ui.utils.ThemeUtil
import ru.exursion.R
import ru.exursion.data.models.SwitchSetting
import ru.exursion.databinding.FragmentDecorSettingsBinding
import ru.exursion.domain.settings.AppSettings
import javax.inject.Inject

class DecorSettingsFragment @Inject constructor(private val appSettings: AppSettings) :
    BaseFragment<FragmentDecorSettingsBinding>(FragmentDecorSettingsBinding::class.java) {

    override fun setUpViews(view: View) = with(binding) {
        val switchSettings = listOf(
            SwitchSetting(getString(R.string.screen_settings_decor_theme), false, ::setDarkTheme)
        )
        val adapter = SettingsAdapter().apply { swapData(switchSettings) }

        header.backButton.setOnClickListener{ findNavController().navigateUp() }
        header.title.text = getString(R.string.screen_profile_decor)
        settingRecycler.adapter = adapter
    }

    private fun setDarkTheme(state: Boolean) {
        appSettings.darkThemeState = state
        ThemeUtil.setDarkThemeState(state)
    }

}