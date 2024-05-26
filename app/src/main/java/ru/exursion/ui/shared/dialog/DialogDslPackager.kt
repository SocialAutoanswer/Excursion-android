package ru.exursion.ui.shared.dialog

import android.app.Dialog
import android.graphics.drawable.Drawable

class DialogDslPackager {

    var title: String? = null
    var summary: String? = null
    var middleIcon: Drawable? = null
    var buttonText: String? = null

    private var onClick: ((Dialog?) -> Unit)? = null
    private var onDismiss: ((Dialog?) -> Unit)? = null

    fun onClick(callback: (Dialog?) -> Unit) {
        onClick = callback
    }

    fun onDismiss(callback: (Dialog?) -> Unit) {
        onDismiss = callback
    }

    fun pack() = ExcDialog().apply {
        title?.let { setTitle(it) }
        summary?.let { setSummary(it) }
        middleIcon?.let { setMiddleIcon(it) }
        buttonText?.let { setButtonText(it) }

        onClick?.let { setOnNeutralButtonClickListener(it) }
        onDismiss?.let { setOnDismiss(it) }
    }

}