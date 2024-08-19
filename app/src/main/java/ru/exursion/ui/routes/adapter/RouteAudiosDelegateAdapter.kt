package ru.exursion.ui.routes.adapter

import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.exursion.data.models.AudioLocation
import ru.exursion.databinding.ItemRouteLocationAudioBinding
import ru.exursion.domain.player.RoutePlayer
import ru.exursion.ui.shared.OnPlayerClickListener

class RouteAudiosDelegateAdapter(private val routePlayer: RoutePlayer) :
    ViewBindingDelegateAdapter<AudioLocation, ItemRouteLocationAudioBinding>(ItemRouteLocationAudioBinding::inflate) {

    private var selectedView: ItemRouteLocationAudioBinding? = null
        set(binding) {
            field?.isPlaying(false)
            binding?.isPlaying(true)
            field = binding
        }

    override fun isForViewType(item: Any): Boolean = item is AudioLocation

    override fun ItemRouteLocationAudioBinding.onBind(item: AudioLocation) {
        val playerClickListener = object : OnPlayerClickListener {
            override fun onPlayClick() {
                if (item.audios.isEmpty()) return

                selectedView = this@onBind
                routePlayer.setCurrentTrack(item.audios[0].audioUrl, item.id)
                routePlayer.onPlayerClickListener.onPlayClick()
            }

            override fun onPauseClick() = routePlayer.onPlayerClickListener.onPauseClick()

            override fun onSetPosition(position: Int) = routePlayer.onPlayerClickListener.onSetPosition(position)
        }

        locationId.setNumber(item.id)
        locationName.text = item.name
        locationDescription.text = item.description
        playBtn.setOnPlayerClickListener(playerClickListener)

        if (routePlayer.isSomeonePlaying && routePlayer.currentPlayingTrackId == item.id) {
            selectedView = this@onBind
        }
    }

    override fun AudioLocation.getItemId() = id

    private fun ItemRouteLocationAudioBinding.isPlaying(isPlaying: Boolean) {
        locationId.setNumberIsSelected(isPlaying)
        playBtn.setUiState(isPlaying)
    }
}