package ru.exursion.ui.shared.ext

import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import ru.exursion.App
import ru.exursion.R
import ru.exursion.ui.shared.dialog.DialogDslPackager
import ru.exursion.ui.shared.dialog.dialog

inline fun <reified F : Fragment> F.inject() {
    val app = (context?.applicationContext as App?) ?: return
    val appComponent = app.appComponent ?: return

    val inject = appComponent::class.java.getMethod("inject", this::class.java)
    inject.invoke(appComponent, this)
}

fun Fragment.networkErrorDialog(lambda: DialogDslPackager.() -> Unit) = dialog("network_error") {
    val context = context ?: return@dialog

    title = getString(R.string.dialog_network_error_title)
    summary = getString(R.string.dialog_network_error_summary)

    middleIcon = AppCompatResources.getDrawable(context, R.drawable.ic_no_network)

    buttonText = getString(R.string.dialog_network_error_neutral_button)

    lambda(this)
}