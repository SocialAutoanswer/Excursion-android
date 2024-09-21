package ru.exursion.ui.events

import android.view.View
import com.bumptech.glide.Glide
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.data.models.Shop
import ru.exursion.databinding.FragmentShopDetailsBinding

class ShopDetailsFragment : BaseFragment<FragmentShopDetailsBinding>(FragmentShopDetailsBinding::class.java) {

    companion object {
        const val SHOP_BUNDLE_KEY = "shop_bundle"
    }

    override fun setUpViews(view: View) {
        val shop = arguments?.getSerializable(SHOP_BUNDLE_KEY) as Shop? ?: return

        binding.shopName.text = shop.name
        binding.address.text = shop.address
        binding.description.text = shop.description

        binding.backButton.setOnClickListener { activity?.onBackPressed() }

        Glide.with(binding.backgroundImage)
            .load(shop.imageUrl)
            .centerCrop()
            .into(binding.backgroundImage)
     }
}