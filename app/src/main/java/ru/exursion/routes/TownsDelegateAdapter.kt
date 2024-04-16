package ru.exursion.routes

import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.databinding.ItemTownBinding

class TownsDelegateAdapter : ViewBindingDelegateAdapter<TownItem, ItemTownBinding>(ItemTownBinding::inflate) {

    override fun ItemTownBinding.onBind(item: TownItem) {
        townName.text = item.name

        Glide.with(backgroundImage)
            .load(item.image)
            .centerCrop()
            .into(backgroundImage)
    }

    override fun TownItem.getItemId(): Any = townId

    override fun isForViewType(item: Any) = item is TownItem
}