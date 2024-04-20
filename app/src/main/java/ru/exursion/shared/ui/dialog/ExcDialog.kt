package ru.exursion.shared.ui.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentDialogExcBinding

class ExcDialog : DialogFragment() {

    private var _binding: FragmentDialogExcBinding? = null
    protected val binding get() = _binding!!

    private var title: String? = null
    private var summary: String? = null
    private var icon: Drawable? = null

    private var buttonText: String? = null

    private var onNeutralButtonClick: ((Dialog?) -> Unit)? = null
    private var onDismissCallback: ((Dialog?) -> Unit)? = null

    fun setTitle(title: String) {
        this.title = title
    }

    fun setMiddleIcon(icon: Drawable) {
        this.icon = icon
    }

    fun setSummary(summary: String) {
        this.summary = summary
    }

    fun setButtonText(text: String) {
        buttonText = text
    }

    fun setOnNeutralButtonClickListener(onClick: (Dialog?) -> Unit) {
        onNeutralButtonClick = onClick
    }

    fun setOnDismiss(callback: (Dialog?) -> Unit) {
        onDismissCallback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ExcDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDialogExcBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title.text = title
        binding.summary.text = summary

        binding.middleIcon.setImageDrawable(icon)

        binding.neutralButton.text = buttonText
        binding.neutralButton.setOnClickListener { onNeutralButtonClick?.invoke(dialog) }

        binding.close.setOnClickListener { dialog?.dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.invoke(this.dialog)
        _binding = null
    }
}