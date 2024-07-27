package ru.exursion.ui.shared.ext

import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import ru.exursion.R
import ru.exursion.data.models.City
import ru.exursion.data.models.Location


const val DEFAULT_ZOOM = 13f
const val DEFAULT_AZIMUTH = 0f
const val DEFAULT_TILT = 0f
const val DEFAULT_LATITUDE = 55.7887
const val DEFAULT_LONGITUDE = 49.1221


fun MapView.move(point: Point) = mapWindow.map.move(
    CameraPosition(
        point,
        DEFAULT_ZOOM,
        DEFAULT_AZIMUTH,
        DEFAULT_TILT
    )
)

fun MapView.setBounds(bounds: BoundingBox) {
    mapWindow.map.cameraBounds.latLngBounds = bounds
}

fun MapView.goToCity(city: City) {
    city.point?.let { move(it) }
}

fun MapView.addPlaceMark(mark: Location, objectTapListener: MapObjectTapListener) {
    val view = ImageView(context).apply {
        background = AppCompatResources.getDrawable(context, R.drawable.ic_audio)
    }

    mapWindow.map.mapObjects.addPlacemark().apply {
        geometry = mark.point
        setView(ViewProvider(view))

        addTapListener(objectTapListener)
    }
}

fun MapView.addPlaceMarks(marks: List<Location>, mapObjectTapListenersMap: HashMap<Long, MapObjectTapListener>) = marks.forEach { location ->
    mapObjectTapListenersMap[location.id]?.let { id -> addPlaceMark(location, id) }
}

fun MapView.setPlaceMarks(marks: List<Location>, objectTapListenersMap: HashMap<Long, MapObjectTapListener>) {
    clearMap()
    addPlaceMarks(marks, objectTapListenersMap)
}

fun MapView.clearMap() = mapWindow.map.mapObjects.clear()