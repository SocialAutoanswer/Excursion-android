package ru.exursion.shared.ui

import androidx.core.view.isVisible
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.R
import ru.exursion.databinding.ItemSelectBinding
import ru.exursion.setDrawable

class SelectItemDelegateAdapter(
    private val showIcon: Boolean,
    private val onItemClick: (SelectItem) -> Unit
) : ViewBindingDelegateAdapter<SelectItem, ItemSelectBinding>(ItemSelectBinding::inflate) {

    override fun ItemSelectBinding.onBind(item: SelectItem) {
        title.text = item.title

        root.setOnClickListener {
            onItemClick(item)
        }

        if (showIcon) {
            startIcon.isVisible = true
            item.image?.also {
                // TODO: implement load of icon
            } ?: run {
                startIcon.setDrawable(R.drawable.ic_cross)
            }
        } else {
            startIcon.isVisible = false
        }
    }

    override fun isForViewType(item: Any) = item is SelectItem

    override fun SelectItem.getItemId() = title

}