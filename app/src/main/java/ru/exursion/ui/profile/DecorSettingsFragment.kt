package ru.exursion.ui.profile

import android.content.Context
import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.ui.utils.ThemeUtil
import ru.exursion.R
import ru.exursion.data.models.SwitchSetting
import ru.exursion.databinding.FragmentDecorSettingsBinding
import ru.exursion.domain.settings.AppSettings
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class DecorSettingsFragment:
    BaseFragment<FragmentDecorSettingsBinding>(FragmentDecorSettingsBinding::class.java) {

    @Inject lateinit var appSettings: AppSettings

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun setUpViews(view: View) = with(binding) {
        val switchSettings = listOf(
            SwitchSetting(getString(R.string.screen_settings_decor_theme), appSettings.darkThemeState, ::setDarkTheme)
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