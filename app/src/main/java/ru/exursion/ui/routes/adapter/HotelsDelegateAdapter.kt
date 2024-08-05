package ru.exursion.ui.routes.adapter

import android.util.Log
import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.Hotel
import ru.exursion.databinding.ItemHotelBinding

class HotelsDelegateAdapter
    : ViewBindingDelegateAdapter<Hotel, ItemHotelBinding>(ItemHotelBinding::inflate){

    private var onClick: ((Hotel) -> Unit)? = null

    fun setOnItemClick(callback: (Hotel) -> Unit) {
        onClick = callback
    }

    override fun isForViewType(item: Any) = item is Hotel

    override fun ItemHotelBinding.onBind(item: Hotel) {
        Log.d("asd", "in adapter $item")
        root.setOnClickListener { onClick?.invoke(item) }

        name.text = item.name
        address.text = item.address
        rating.rating = item.rating

        Glide.with(backgroundImage)
            .load(item.imageUrl)
            .centerCrop()
            .into(backgroundImage)
    }

    override fun Hotel.getItemId(): Any = id

}