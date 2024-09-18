package ru.exursion.ui.map

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.yandex.mapkit.map.MapObjectTapListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.City
import ru.exursion.data.models.Location
import ru.exursion.domain.CitiesUseCase
import ru.exursion.domain.FavoritesUseCase
import ru.exursion.domain.LocationUseCase
import ru.exursion.domain.player.MapPlayer
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val citiesUseCase: CitiesUseCase,
    private val locationsUseCase: LocationUseCase,
    private val favoritesUseCase: FavoritesUseCase,
    private val mapPlayer: MapPlayer
): RxStateViewModel<MapViewModel.MapState, MapViewModel.MapEffect>() {

    private val objectTapListeners = arrayListOf<MapObjectTapListener>()

    var chosenCityPosition: Int? = null

    fun getPointPlayerClickListener() = mapPlayer.pointPlayerClickListener

    fun getMapPlayerClickListener() = mapPlayer.mapPlayerClickListener

    fun getPointIsPlaying() = mapPlayer.candidateIsPlaying()

    fun getPointTrackIsCurrent() = mapPlayer.candidateIsCurrent()

    fun getIsSomeonePlaying() = mapPlayer.isSomeonePlaying

    fun addTapListener(locationId: Long): MapObjectTapListener {
        val listener = MapObjectTapListener { _, _ ->
            getLocationById(locationId)
            true
        }

        objectTapListeners.add(listener)
        return listener
    }

    fun setIdleState() {
        _state.value = MapState.Idle
    }

    fun setOnPlayerTimerListener(callback: (Int) -> Unit) = invokeDisposable {
        mapPlayer.observePlayerTimer()
            .subscribe({
                if (getPointTrackIsCurrent()) {
                    callback(it)
                }
            }, {
                _effect.postValue(MapEffect.Error)
            })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getCities() = invokeDisposable {
        citiesUseCase.getCities()
            .cachedIn(viewModelScope)
            .subscribe({ citiesData ->
                _state.postValue(MapState.CitiesReceived(citiesData))
            }, {
                _effect.postValue(MapEffect.Error)
                _state.postValue(MapState.Idle)
            })
    }

    fun getLocationsByCity(cityId: Long) = invokeDisposable {
        locationsUseCase.getLocations(cityId)
            .doOnSubscribe { _state.postValue(MapState.Loading) }
            .subscribe({ locations ->
                _state.postValue(MapState.LocationsReceived(locations))
            }, {
                _effect.postValue(MapEffect.Error)
                _state.postValue(MapState.Idle)
            })
    }

    fun changeLocationFavoriteState(locationId: Long) = invokeDisposable {
        favoritesUseCase.changeLocationFavoriteState(locationId)
            .doOnSubscribe{ _state.postValue(MapState.LikeLoading) }
            .subscribe({
                _state.postValue(MapState.FavoriteStateChanged(it.content))
            }, {
                _effect.postValue(MapEffect.Error)
                _state.postValue(MapState.Idle)
            })
    }

    fun getLocationById(locationId: Long) = invokeDisposable {
        locationsUseCase.getLocationById(locationId)
            .doOnSubscribe{ _state.postValue(MapState.Loading) }
            .subscribe({
                mapPlayer.setTrackCandidate(
                    if (it.audios.isNotEmpty()) it.audios[0].audioUrl
                    else null
                )
                _state.postValue(MapState.AudioLocationReceived(it))
            }, {
                _effect.postValue(MapEffect.Error)
            })
    }

    sealed class MapState: State {
        data object Idle : MapState()
        data object Loading : MapState()
        data object LikeLoading : MapState()
        class CitiesReceived(val citiesData: PagingData<City>) : MapState()
        class LocationsReceived(val locations: List<Location>) : MapState()
        class AudioLocationReceived(val audioLocation: AudioLocation) : MapState()
        class FavoriteStateChanged(val message: String) : MapState()
    }

    sealed class MapEffect: Effect {
        data object Error : MapEffect()
    }

}