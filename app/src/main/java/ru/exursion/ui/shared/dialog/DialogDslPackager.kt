package ru.exursion.ui.shared.dialog

import android.app.Dialog
import android.graphics.drawable.Drawable

class DialogDslPackager {

    var title: String? = null
    var secondaryTitle: String? = null
    var summary: String? = null
    var middleIcon: Drawable? = null
    var neutralButtonText: String? = null
    var rejectButtonText: String? = null
    var confirmButtonText: String? = null
    var titleIsBold: Boolean? = null

    private var onNeutralClick: ((Dialog?) -> Unit)? = null
    private var onDismiss: ((Dialog?) -> Unit)? = null
    private var onConfirm: ((Dialog?) -> Unit)? = null

    fun onNeutralClick(callback: (Dialog?) -> Unit) {
        onNeutralClick = callback
    }

    fun onDismiss(callback: (Dialog?) -> Unit) {
        onDismiss = callback
    }

    fun onConfirm(callback: (Dialog?) -> Unit) {
        onConfirm = callback
    }

    fun pack() = ExcDialog().apply {
        title?.let { setTitle(it) }
        secondaryTitle?.let { setSecondaryTitle(it) }
        summary?.let { setSummary(it) }
        middleIcon?.let { setMiddleIcon(it) }
        neutralButtonText?.let { setNeutralButtonText(it) }
        rejectButtonText?.let { setRejectButtonText(it) }
        confirmButtonText?.let { setConfirmButtonText(it) }
        titleIsBold?.let { setTitleIsBold(it) }

        onNeutralClick?.let { setOnNeutralButtonClickListener(it) }
        onDismiss?.let { setOnDismiss(it) }
        onConfirm?.let { setOnConfirmButtonClick(it) }
    }

}