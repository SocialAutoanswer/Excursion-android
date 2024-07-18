package ru.exursion.ui.shared.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import ru.exursion.R
import ru.exursion.ui.shared.dialog.DialogDslPackager
import ru.exursion.ui.shared.dialog.dialog

fun AppCompatActivity.networkErrorDialog(lambda: DialogDslPackager.() -> Unit) = dialog(supportFragmentManager,"network_error") {

    title = getString(R.string.dialog_network_error_title)
    summary = getString(R.string.dialog_network_error_summary)

    middleIcon = AppCompatResources.getDrawable(this@networkErrorDialog, R.drawable.ic_no_network)

    neutralButtonText = getString(R.string.dialog_network_error_neutral_button)

    lambda(this)
}