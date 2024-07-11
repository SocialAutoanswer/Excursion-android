package ru.exursion.ui.profile

import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.SwitchSetting
import ru.exursion.databinding.ItemSwitchSettingBinding

class SwitchSettingsAdapter: ViewBindingDelegateAdapter<SwitchSetting, ItemSwitchSettingBinding>(ItemSwitchSettingBinding::inflate) {
    override fun isForViewType(item: Any): Boolean = item is SwitchSetting

    override fun ItemSwitchSettingBinding.onBind(item: SwitchSetting) {
        settingName.text = item.title
        settingSwitch.isChecked = item.state
        settingSwitch.setOnCheckedChangeListener(item.onCheckedChangedListener)
    }

    override fun SwitchSetting.getItemId(): Any = title.hashCode()

}

class SettingsAdapter: CompositeDelegateAdapter(SwitchSettingsAdapter())