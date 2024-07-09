package ru.exursion.data.models

import android.widget.CompoundButton

data class SwitchSetting(
    val title: String,
    val state: Boolean,
    private val stateChangeCallback: (Boolean) -> Unit
) {
    val onCheckedChangedListener =
        CompoundButton.OnCheckedChangeListener { _, isChecked ->  stateChangeCallback(isChecked)}
}