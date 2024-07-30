package ru.exursion.ui.map

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.yandex.mapkit.MapKitFactory
import ru.bibaboba.kit.states.StateMachine
import ru.bibaboba.kit.ui.StateFragment
import ru.exursion.R
import ru.exursion.data.models.City
import ru.exursion.databinding.FragmentMapBinding
import ru.exursion.ui.shared.ext.addItemMargins
import ru.exursion.ui.shared.ext.goToCity
import ru.exursion.ui.shared.ext.inject
import ru.exursion.ui.shared.ext.setBounds
import ru.exursion.ui.shared.ext.setPlaceMarks
import javax.inject.Inject

class MapFragment : StateFragment<FragmentMapBinding, MapViewModel>(FragmentMapBinding::class.java) {

    private var adapter: CityAdapter? = null

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    override val viewModel by activityViewModels<MapViewModel> { viewModelFactory }

    override val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addLoadingState()
        .addCitiesReceivedState()
        .addLocationsReceivedState()
        .addAudioLocationReceivedState()
        .addErrorEffect()
        .build()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
        viewModel.getCities()
    }

    override fun setUpViews(view: View): Unit = with(binding) {
        adapter = CityAdapter(::changeCity, viewModel.chosenCityPosition)

        cityRecycler.also {
            it.adapter = adapter
            it.addItemMargins(26, 0)
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
        viewModel.chosenCityPosition = adapter?.currentPosition
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
            adapter?.submitData(lifecycle, it.citiesData)
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
            val tag = "location-${it.audioLocation.id}"

            val dialog = (parentFragmentManager
                .findFragmentByTag(tag))

            if (dialog != null && dialog.isVisible) return@addState

            if (it.audioLocation.audios.isNotEmpty()) {
                binding.playerView.setTrackName(it.audioLocation.audios[0].name)
            }

            LocationBottomDialog()
                .apply {
                    setOnDismiss {
                        setUpPlayer()
                        viewModel.effect.observe(viewLifecycleOwner, stateMachine::submit)
                    }
                }
                .show(parentFragmentManager, tag)

            viewModel.effect.removeObservers(viewLifecycleOwner)
            viewModel.setIdleState()
        }
    }


    private fun StateMachine.Builder.addErrorEffect(): StateMachine.Builder {
        return addEffect(MapViewModel.MapEffect.Error::class) {
            Toast.makeText(context, R.string.undefined_error, Toast.LENGTH_LONG).show()
        }
    }

}