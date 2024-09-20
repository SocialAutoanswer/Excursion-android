package ru.exursion.ui.events

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import ru.exursion.R

class CitiesPopupAdapter(
    context: Context,
    private val onCityNameClick: (String) -> Unit
) : ArrayAdapter<String>(context, R.layout.item_city_popup) {

    //TODO: заменить это на что-то адекватное, но у меня сроки жмут и вообще тут сильно производительный скролл не нужен
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_city_popup, parent, false)
            .apply {
                findViewById<TextView>(R.id.city_name).apply {
                    getItem(position)?.let { name ->
                        text = name
                        setOnClickListener { onCityNameClick(name) }
                    } ?: run { isVisible = false }
                }
                findViewById<View>(R.id.divider).apply { isVisible = position != count - 1 }
            }
    }
}