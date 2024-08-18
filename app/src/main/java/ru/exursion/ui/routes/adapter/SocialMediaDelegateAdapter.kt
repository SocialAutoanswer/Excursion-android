package ru.exursion.ui.routes.adapter

import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.bibaboba.kit.ui.getDrawableByName
import ru.exursion.data.models.SocialMedia
import ru.exursion.databinding.ItemSocialMediaBinding
import ru.exursion.domain.AppNavigator

class SocialMediaDelegateAdapter : ViewBindingDelegateAdapter<SocialMedia, ItemSocialMediaBinding>(ItemSocialMediaBinding::inflate) {

    override fun isForViewType(item: Any): Boolean = item is SocialMedia

    override fun ItemSocialMediaBinding.onBind(item: SocialMedia) {
        socialMediaIcon.setImageDrawable(
            root.context.getDrawableByName(item.iconName)
        )
        socialMediaAddress.text = item.name

        root.setOnClickListener {
            if (item.socialUrl.contains("+7")) {
                AppNavigator.Phone(item.name).navigate(root.context::startActivity)
            }

            if (item.socialUrl.contains("vk")) {
                AppNavigator.Vkontakte(item.name).navigate(root.context::startActivity)
            }

            if (item.socialUrl.contains(".me")) {
               AppNavigator.Telegram(item.name).navigate(root.context::startActivity)
            }
        }
    }

    override fun SocialMedia.getItemId(): Any = id


}