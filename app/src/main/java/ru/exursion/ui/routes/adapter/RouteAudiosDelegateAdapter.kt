package ru.exursion.ui.routes.adapter

import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.AudioLocation
import ru.exursion.databinding.ItemRouteLocationAudioBinding

class RouteAudiosDelegateAdapter(private var currentPlayingId: Long?) :
    ViewBindingDelegateAdapter<AudioLocation, ItemRouteLocationAudioBinding>(ItemRouteLocationAudioBinding::inflate) {

    override fun isForViewType(item: Any): Boolean = item is AudioLocation

    override fun ItemRouteLocationAudioBinding.onBind(item: AudioLocation) {
        locationId.setNumber(item.id)
        locationName.text = item.name
        locationDescription.text = item.description

        playBtn.setOnClickListener {
            locationId.setNumberIsSelected(true)
        }
        locationId.setNumberIsSelected(item.getItemId() == currentPlayingId)
    }

    override fun AudioLocation.getItemId() = id
}