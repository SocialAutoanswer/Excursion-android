package ru.exursion.data.recommendations

import ru.bibaboba.kit.util.Mapper
import ru.exursion.data.models.Shop
import ru.exursion.data.models.ShopDto
import javax.inject.Inject

class ShopsMapper @Inject constructor() : Mapper<ShopDto, Shop> {
    override fun map(input: ShopDto) = Shop(
        id = input.id ?: -1,
        name = input.name ?: "",
        description = input.description ?: "",
        isRecommended = input.isRecommended ?: false,
        address = input.address ?: "",
        cityId = input.cityId ?: -1,
        imageUrl = "https://as2.ftcdn.net/v2/jpg/00/53/45/31/1000_F_53453175_hVgYVz0WmvOXPd9CNzaUcwcibiGao3CL.jpg"
    )
}