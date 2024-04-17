package ru.exursion.routes

import com.bumptech.glide.Glide
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.databinding.ItemTownBinding

class TownsDelegateAdapter(
    private val onTownClick: (TownItem) -> Unit
) : ViewBindingDelegateAdapter<TownItem, ItemTownBinding>(ItemTownBinding::inflate) {

    override fun ItemTownBinding.onBind(item: TownItem) {
        townName.text = item.name
        root.setOnClickListener { onTownClick(item) }

        Glide.with(backgroundImage)
            .load(item.image)
            .centerCrop()
            .into(backgroundImage)
    }

    override fun TownItem.getItemId(): Any = townId

    override fun isForViewType(item: Any) = item is TownItem
}