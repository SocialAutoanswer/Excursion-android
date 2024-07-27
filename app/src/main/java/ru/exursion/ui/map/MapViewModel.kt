package ru.exursion.ui.map

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.map.MapObjectTapListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.bibaboba.kit.RxStateViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.bibaboba.kit.util.asLiveData
import ru.exursion.data.models.AudioLocation
import ru.exursion.data.models.City
import ru.exursion.data.models.Location
import ru.exursion.domain.CitiesUseCase
import ru.exursion.domain.LocationUseCase
import ru.exursion.domain.player.MapPlayer
import javax.inject.Inject

class MapViewModel @Inject constructor(
    private val citiesUseCase: CitiesUseCase,
    private val locationsUseCase: LocationUseCase,
    private val mapPlayer: MapPlayer
): RxStateViewModel<MapViewModel.MapState, MapViewModel.MapEffect>() {

    private val _cityBoundingBox = MutableLiveData<BoundingBox>()
    val cityBoundingBox = _cityBoundingBox.asLiveData()

    val objectTapListenersMap = HashMap<Long, MapObjectTapListener>()

    fun getOnPlayerClickListener() = mapPlayer.onPlayerClickListener

    fun getDoIPlay() = mapPlayer.doIPlay

    fun getIsSomeonePlaying() = mapPlayer.isSomeonePlaying

    fun addTapListener(locationId: Long) {
        objectTapListenersMap[locationId] = MapObjectTapListener { _, _ ->
            getLocationById(locationId)
            true
        }
    }

    @SuppressLint("CheckResult")
    fun setOnPlayerTimerListener(callback: (Int) -> Unit) = invokeDisposable {
        mapPlayer.observePlayerTimer()
            .subscribe {
                if (mapPlayer.doIPlay) {
                    callback(it)
                }
            }
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
        locationsUseCase.getLocationsByCity(cityId)
            .doOnSubscribe { _state.postValue(MapState.Loading) }
            .doAfterSuccess{ locations ->
                _cityBoundingBox.postValue(
                    locationsUseCase.getCityBoundingBox(locations.map{ it.point })
                )
            }
            .subscribe({ locations ->
                _state.postValue(MapState.LocationsReceived(locations))
            }, {
                _effect.postValue(MapEffect.Error)
                _state.postValue(MapState.Idle)
            })
    }

    private fun getLocationById(locationId: Long) = invokeDisposable {
        locationsUseCase.getLocationById(locationId)
            .doOnSubscribe{ _state.postValue(MapState.Loading) }
            .subscribe({
                mapPlayer.setTrackCandidate(it.audios[0].audioUrl)
                _state.postValue(MapState.AudioLocationReceived(it))
            }, {
                _effect.postValue(MapEffect.Error)
            })
    }

    sealed class MapState: State {
        data object Idle : MapState()
        data object Loading : MapState()
        class CitiesReceived(val citiesData: PagingData<City>) : MapState()
        class LocationsReceived(val locations: List<Location>) : MapState()
        class AudioLocationReceived(val audioLocation: AudioLocation) : MapState()
    }

    sealed class MapEffect: Effect {
        data object Error : MapEffect()
    }

}