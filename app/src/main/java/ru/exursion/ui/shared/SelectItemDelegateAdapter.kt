package ru.exursion.ui.shared

import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.bibaboba.kit.ui.getDrawableByName
import ru.exursion.databinding.ItemSelectBinding
import ru.exursion.ui.shared.ext.setStartDrawable

class SelectItemDelegateAdapter(
    private val onItemClick: (SelectItem) -> Unit
) : ViewBindingDelegateAdapter<SelectItem, ItemSelectBinding>(ItemSelectBinding::inflate) {

    override fun ItemSelectBinding.onBind(item: SelectItem) {
        root.text = item.title

        root.setStartDrawable(
            item.image?.let {
                root.context.getDrawableByName(it)
            }
        )

        root.setOnClickListener {
            onItemClick(item)
        }

    }

    override fun isForViewType(item: Any) = item is SelectItem

    override fun SelectItem.getItemId() = title

}