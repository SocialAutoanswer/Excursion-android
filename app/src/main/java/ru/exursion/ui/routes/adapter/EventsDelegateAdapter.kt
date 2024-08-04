package ru.exursion.ui.routes.adapter

import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.Event
import ru.exursion.databinding.ItemEventBinding

class EventsDelegateAdapter: ViewBindingDelegateAdapter<Event, ItemEventBinding>(ItemEventBinding::inflate){

    private var onClick: ((Event) -> Unit)? = null

    fun setOnItemClick(callback: (Event) -> Unit) {
        onClick = callback
    }

    override fun isForViewType(item: Any) = item is Event

    override fun ItemEventBinding.onBind(item: Event) {
        root.setOnClickListener { onClick?.invoke(item) }

        name.text = item.name
        address.text = item.address
        date.text = item.date

        Glide.with(backgroundImage)
            .load(item.imageUrl)
            .centerCrop()
            .into(backgroundImage)
    }

    override fun Event.getItemId(): Any = id

}