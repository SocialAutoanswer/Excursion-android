package ru.exursion.ui.shared

import androidx.core.view.isVisible
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.bibaboba.kit.ui.getDrawableByName
import ru.exursion.R
import ru.exursion.databinding.ItemSelectBinding
import ru.exursion.ui.shared.ext.setDrawable
import ru.exursion.ui.shared.ext.setDrawableStart

class SelectItemDelegateAdapter(
    private val onItemClick: (SelectItem) -> Unit
) : ViewBindingDelegateAdapter<SelectItem, ItemSelectBinding>(ItemSelectBinding::inflate) {

    override fun ItemSelectBinding.onBind(item: SelectItem) {
        root.text = item.title

        root.setOnClickListener {
            onItemClick(item)
        }

        item.image?.also {
            root.context.getDrawableByName(item.image)?.let { drawable ->
                root.setDrawableStart(drawable)
            }
        } ?: run {
            root.setDrawableStart(R.drawable.ic_cross)
        }
    }

    override fun isForViewType(item: Any) = item is SelectItem

    override fun SelectItem.getItemId() = title

}