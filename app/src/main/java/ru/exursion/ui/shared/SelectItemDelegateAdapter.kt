package ru.exursion.ui.shared

import androidx.core.view.isVisible
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.R
import ru.exursion.databinding.ItemSelectBinding
import ru.exursion.ui.shared.ext.setDrawable

class SelectItemDelegateAdapter(
    private val onItemClick: (SelectItem) -> Unit
) : ViewBindingDelegateAdapter<SelectItem, ItemSelectBinding>(ItemSelectBinding::inflate) {

    override fun ItemSelectBinding.onBind(item: SelectItem) {
        root.text = item.title

        root.setOnClickListener {
            onItemClick(item)
        }

    }

    override fun isForViewType(item: Any) = item is SelectItem

    override fun SelectItem.getItemId() = title

}