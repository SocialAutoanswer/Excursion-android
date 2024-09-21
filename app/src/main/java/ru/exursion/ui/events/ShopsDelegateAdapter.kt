package ru.exursion.ui.events

import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.Shop
import ru.exursion.databinding.ItemShopBinding

class ShopsDelegateAdapter(
    private val onItemClick: (Shop) -> Unit
) : ViewBindingDelegateAdapter<Shop, ItemShopBinding>(ItemShopBinding::inflate) {

    override fun isForViewType(item: Any) = item is Shop

    override fun ItemShopBinding.onBind(item: Shop) {
        root.setOnClickListener { onItemClick(item) }

        name.text = item.name

        Glide.with(backgroundImage)
            .load(item.imageUrl)
            .centerCrop()
            .into(backgroundImage)
    }

    override fun Shop.getItemId() = id
}