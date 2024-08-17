package ru.exursion.ui.routes

interface RouteActivityController {
    fun setButtonUiState(state: RouteDetailsActivity.ButtonState)
    fun setOnPayButtonClick(callback: () -> Unit)
}