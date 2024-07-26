package ru.exursion.ui.map

import android.content.Context
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.MapKitFactory
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.data.models.City
import ru.exursion.databinding.FragmentMapBinding
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.goToCity
import ru.exursion.ui.shared.ext.inject
import ru.exursion.ui.shared.ext.setBounds
import ru.exursion.ui.shared.ext.setPlaceMarks
import javax.inject.Inject

class MapFragment : StateFragment<FragmentMapBinding, MapViewModel>(FragmentMapBinding::class.java) {

    private val adapter = CityAdapter(::changeCity)

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by activityViewModels<MapViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .addLoadingState()
        .addCitiesReceivedState()
        .addLocationsReceivedState()
        .addAudioLocationReceivedState()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
        viewModel.getCities()
    }

    override fun setUpViews(view: View): Unit = with(binding) {
        cityRecycler.also {
            it.adapter = adapter
            it.addItemMargins(27, 0)
        }

        setUpPlayer()
    }


    override fun setUpObservers() {
        super.setUpObservers()

        viewModel.cityBoundingBox.observe(viewLifecycleOwner) {
            binding.mapView.setBounds(it)
        }
    }


    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        binding.mapView.onStop()
        super.onStop()
    }

    private fun changeCity(city: City) {
        binding.mapView.goToCity(city)
        viewModel.getLocationsByCity(city.id)
    }

    private fun setUpPlayer() {
        binding.playerView.isVisible = viewModel.getIsSomeonePlaying()
        binding.playerView.setPlayerUI(viewModel.getIsSomeonePlaying())
        binding.playerView.setOnPlayerClickListener(viewModel.getOnPlayerClickListener())

        viewModel.setOnPlayerTimerListener(binding.playerView::setCurrentPosition)
    }


    private fun StateMachine.Builder.addLoadingState(): StateMachine.Builder {
        return addState(
            MapViewModel.MapState.Loading::class,
            callback = { binding.loading.root.isVisible = true },
            onExit = { binding.loading.root.isVisible = false },
        )
    }

    private fun StateMachine.Builder.addCitiesReceivedState(): StateMachine.Builder {
        return addState(MapViewModel.MapState.CitiesReceived::class) {
            adapter.submitData(lifecycle, it.citiesData)
        }
    }

    private fun StateMachine.Builder.addLocationsReceivedState(): StateMachine.Builder {
        return addState(MapViewModel.MapState.LocationsReceived::class) {
            it.locations.forEach { location ->
                viewModel.addTapListener(location.id)
            }

            binding.mapView.setPlaceMarks(it.locations, viewModel.objectTapListenersMap)
        }
    }

    private fun StateMachine.Builder.addAudioLocationReceivedState(): StateMachine.Builder {
        return addState(MapViewModel.MapState.AudioLocationReceived::class) {
            LocationBottomDialog()
                .apply {
                    setOnDismiss { setUpPlayer() }
                }
                .show(parentFragmentManager, "location-${it.audioLocation.id}")
        }
    }


    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(MapViewModel.MapEffect.Error::class) {
        }
    }

}