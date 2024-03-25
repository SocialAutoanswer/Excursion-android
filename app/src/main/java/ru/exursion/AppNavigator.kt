package ru.exursion

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

sealed class AppNavigator(
    private val _action: String,
    private val _data: Uri? = null,
    private val _package: String? = null
) {

    class WhatsApp(phone: String) : AppNavigator(
        Intent.ACTION_VIEW,
        Uri.parse("http://api.whatsapp.com/send?phone=$phone"),
        "com.whatsapp"
    )

    class Telegram(userId: String) : AppNavigator(
        Intent.ACTION_VIEW,
        Uri.parse("https://telegram.me/$userId"),
        "org.telegram.messenger"
    )

    class Phone(phone: String) : AppNavigator(
        Intent.ACTION_DIAL,
        Uri.parse("tel:$phone")
    )

    class Vkontakte(profileId: String) : AppNavigator(
        Intent.ACTION_VIEW,
        Uri.parse("https://vk.com/$profileId")
    )


    private fun createIntent() = Intent().apply {
        action = _action
        data = _data
        `package` = _package
    }

    open fun navigate(startActivity: (Intent) -> Unit) {
        try {
            startActivity(createIntent())
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$_package")
                )
            )
        }
    }

}