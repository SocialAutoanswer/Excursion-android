package ru.exursion.shared.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import ru.exursion.R

class ExcSearchView(
    context: Context,
    attributeSet: AttributeSet? = null
) : FrameLayout(context, attributeSet, R.attr.exc_search_view_style) {

    private val inputContainer: LinearLayout
    private val editTextView: EditText
    private val endIconView: ImageView
    private val errorTextView: TextView

    private var errorBackground: Drawable? = null

    private var defaultHintColor: Int? = null
    private var errorColor: Int? = null

    init {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_exc_search, this, false)

        addView(view)

        inputContainer = view.findViewById(R.id.input_container)
        editTextView = view.findViewById(R.id.search_edit)
        endIconView = view.findViewById(R.id.end_icon)
        errorTextView = view.findViewById(R.id.error_text)

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ExcSearchView, R.attr.exc_search_view_style, 0)

        errorBackground = typedArray.getDrawable(R.styleable.ExcSearchView_editErrorBackground)
        errorColor = typedArray.getColor(R.styleable.ExcSearchView_errorColor, R.attr.exc_text_color_error)
        defaultHintColor = typedArray.getColor(R.styleable.ExcSearchView_android_textColorHint, R.attr.exc_text_color_hint)

        inputContainer.also {
            it.background = typedArray.getDrawable(R.styleable.ExcSearchView_editBackground)
        }

        editTextView.also {
            it.setText(typedArray.getString(R.styleable.ExcSearchView_android_text))
            it.hint = typedArray.getString(R.styleable.ExcSearchView_android_hint)
            defaultHintColor?.also { color -> it.setHintTextColor(color) }
        }

        errorTextView.also {
            errorColor?.also { color -> it.setTextColor(color) }
            it.text = typedArray.getText(R.styleable.ExcSearchView_errorText)
        }

        endIconView.setImageDrawable(typedArray.getDrawable(R.styleable.ExcSearchView_endIcon))

        typedArray.recycle()
    }

    fun showError(@StringRes errorText: Int) {
        errorColor?.also { color -> editTextView.setHintTextColor(color) }
        errorTextView.setText(errorText)
        errorTextView.isVisible = true
    }

    fun hideError() {
        defaultHintColor?.also { color -> editTextView.setHintTextColor(color) }
        errorTextView.isVisible = false
    }

    fun addOnTextChangedListener(listener: (String) -> Unit) {
        editTextView.addTextChangedListener { listener(it.toString()) }
    }

}