package ru.exursion.domain.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    companion object {
        private const val PREFERENCES_FILE_NAME = "excursion_prefs"
    }

    @Provides
    fun provideSecureSharedPreferences(context: Context): SharedPreferences = EncryptedSharedPreferences.create(
        PREFERENCES_FILE_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    @Provides
    fun provideUserSettings(
        sharedPreferences: SharedPreferences
    ): UserSettings = UserSettingsImpl(sharedPreferences)

    @Provides
    fun provideAppSettings(
        sharedPreferences: SharedPreferences
    ): AppSettings = AppSettingsImpl(sharedPreferences)

}