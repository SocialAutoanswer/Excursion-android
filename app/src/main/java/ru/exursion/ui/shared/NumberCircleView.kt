package ru.exursion.ui.shared

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import ru.exursion.databinding.ViewNumberedPointBinding

class NumberCircleView(context: Context, attributeSet: AttributeSet?): LinearLayout(context, attributeSet) {

    private val binding = ViewNumberedPointBinding.inflate(LayoutInflater.from(context), this, true)
    private var isTriangleVisible = false
    private var numberIsSelected = false
    private var number: Long = 1

    init {
        binding.number.text = number.toString()
        binding.triangle.isVisible = isTriangleVisible
        binding.number.isSelected = numberIsSelected
    }

    fun setTriangleIsVisible(isVisible: Boolean) {
        isTriangleVisible = isVisible
        binding.triangle.isVisible = isTriangleVisible
    }

    fun setNumber(number: Long) {
        this.number = number
        binding.number.text = number.toString()
    }

    fun setNumberIsSelected(isSelected: Boolean) {
        numberIsSelected = isSelected
        binding.number.isSelected = numberIsSelected
    }

    override fun onSaveInstanceState(): Parcelable = Bundle().apply {
        putParcelable("superState", super.onSaveInstanceState())
        putBoolean("isSelected", numberIsSelected)
        putLong("number", number)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var _state = state

        if (state is Bundle) {
            numberIsSelected = state.getBoolean("isSelected")
            number = state.getLong("number")
            _state = state.getParcelable("superState")
        }
        super.onRestoreInstanceState(_state)
    }
}