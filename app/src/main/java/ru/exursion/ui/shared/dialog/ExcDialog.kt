package ru.exursion.ui.shared.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentDialogExcBinding

class ExcDialog : DialogFragment() {

    private var _binding: FragmentDialogExcBinding? = null
    protected val binding get() = _binding!!

    private var title: String? = null
    private var secondaryTitle: String? = null
    private var summary: String? = null
    private var icon: Drawable? = null
    private var titleIsBold: Boolean? = null

    private var neutralButtonText: String? = null
    private var rejectButtonText: String? = null
    private var confirmButtonText: String? = null

    private var onNeutralButtonClick: ((Dialog?) -> Unit)? = null
    private var onDismissCallback: ((Dialog?) -> Unit)? = null
    private var onConfirmCallback: ((Dialog?) -> Unit)? = null

    fun setTitle(title: String) {
        this.title = title
    }

    fun setMiddleIcon(icon: Drawable) {
        this.icon = icon
    }

    fun setSummary(summary: String) {
        this.summary = summary
    }

    fun setSecondaryTitle(secondaryTitle: String) {
        this.secondaryTitle = secondaryTitle
    }

    fun setRejectButtonText(rejectButtonText: String) {
        this.rejectButtonText = rejectButtonText
    }

    fun setConfirmButtonText(confirmButtonText: String) {
        this.confirmButtonText = confirmButtonText
    }

    fun setNeutralButtonText(text: String) {
        neutralButtonText = text
    }

    fun setOnNeutralButtonClickListener(onClick: (Dialog?) -> Unit) {
        onNeutralButtonClick = onClick
    }

    fun setOnDismiss(callback: (Dialog?) -> Unit) {
        onDismissCallback = callback
    }

    fun setOnConfirmButtonClick(callback: (Dialog?) -> Unit) {
        onConfirmCallback = callback
    }

    fun setTitleIsBold(state: Boolean) {
        titleIsBold = state
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

        setUpView(binding.title, title, null)
        setUpView(binding.secondaryTitle, secondaryTitle, null)
        setUpView(binding.summary, summary, null)

        icon?.let {
            binding.middleIcon.setImageDrawable(it)
            binding.middleIcon.visibility = View.VISIBLE
        }

        if(titleIsBold == true) {
            context?.let {
                binding.title.typeface = ResourcesCompat.getFont(it, R.font.roboto_bold)
            }
        }

        setUpView(binding.neutralButton, neutralButtonText, onNeutralButtonClick)
        setUpView(binding.rejectButton, rejectButtonText, onDismissCallback)
        setUpView(binding.confirmButton, confirmButtonText, onConfirmCallback)

        binding.close.setOnClickListener { dialog?.dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.invoke(this.dialog)
        _binding = null
    }

    private fun setUpView(view: TextView, text: String?, clickListener: ((Dialog?) -> Unit)?) {
        text?.let {
            view.visibility = View.VISIBLE
            view.text = it
        }

        if(view is Button) {
            clickListener?.let { listener ->
                view.setOnClickListener { listener.invoke(this.dialog) }
            }
        }
    }
}