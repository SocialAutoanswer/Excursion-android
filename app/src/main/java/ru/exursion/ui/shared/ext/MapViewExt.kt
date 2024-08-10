package ru.exursion.ui.shared.ext

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import ru.exursion.R
import ru.exursion.data.models.City
import ru.exursion.ui.shared.NumberCircleView


const val DEFAULT_ZOOM = 13f
const val DEFAULT_AZIMUTH = 0f
const val DEFAULT_TILT = 0f
const val DEFAULT_LATITUDE = 55.7887
const val DEFAULT_LONGITUDE = 49.1221

const val MAX_LATITUDE = 89.3
const val MIN_LATITUDE = -89.3
const val MAX_LONGITUDE = 179.99
const val MIN_LONGITUDE = -180.0

//coordinates from documentation https://yandex.ru/dev/mapkit/doc/ru/com/yandex/mapkit/map/CameraBounds

enum class MarkType {
    AUDIO, NUMBERED
}

data class PlaceMark(
    val id: Long,
    val point: Point,
    val markType: MarkType,
    val tapListener: MapObjectTapListener? = null
)

private fun MapView.getViewByMarkType(markType: MarkType, markId: Long): View = when(markType) {
    MarkType.AUDIO -> View(context).apply {background = AppCompatResources.getDrawable(context, R.drawable.ic_audio) }
    MarkType.NUMBERED -> NumberCircleView(context).apply {
        setNumber(markId)
        setTriangleIsVisible(true)
    }
}

fun MapView.move(point: Point, zoom: Float? = null) = mapWindow.map.move(
    CameraPosition(
        point,
        zoom ?: DEFAULT_ZOOM,
        DEFAULT_AZIMUTH,
        DEFAULT_TILT
    )
)

fun MapView.setBoundsByPoints(points: List<Point>) {
    mapWindow.map.cameraBounds.latLngBounds = getBoundingBoxByPoints(points)
}

fun MapView.goToCity(city: City) {
    city.point?.let { move(it) }
}

fun MapView.createRoute(placeMarks: List<PlaceMark>) {
    if (placeMarks.isEmpty()) return

    val points = placeMarks.map { it.point }
    setBoundsByPoints(points)
    mapWindow.map.mapObjects.addPolyline(Polyline(points))
    addPlaceMarks(placeMarks)
    move(points[0], 25f)
}

fun MapView.addPlaceMark(placeMark: PlaceMark) {
    mapWindow.map.mapObjects.addPlacemark().apply {
        geometry = placeMark.point
        setView(ViewProvider(getViewByMarkType(placeMark.markType, placeMark.id)))

        placeMark.tapListener?.let { addTapListener(it) }
    }
}

fun MapView.addPlaceMarks(placeMarks: List<PlaceMark>) =
    placeMarks.forEach { mark ->
        addPlaceMark(mark)
    }

fun MapView.setPlaceMarks(placeMarks: List<PlaceMark>) {
    clearMap()
    addPlaceMarks(placeMarks)
}

fun MapView.clearMap() = mapWindow.map.mapObjects.clear()


private fun getBoundingBoxByPoints(points: List<Point>): BoundingBox {
    if (points.isEmpty()) {
        return BoundingBox(
            Point(MIN_LATITUDE, MIN_LONGITUDE),
            Point(MAX_LATITUDE, MAX_LONGITUDE)
        )
    }

    var minLatitude = MAX_LATITUDE
    var maxLatitude = MIN_LATITUDE

    var maxLongitude = MIN_LONGITUDE
    var minLongitude = MAX_LONGITUDE

    points.forEach { point ->
        if(point.latitude < minLatitude) minLatitude = point.latitude
        if(point.latitude > maxLatitude) maxLatitude = point.latitude

        if(point.longitude < minLongitude) minLongitude = point.longitude
        if(point.longitude > maxLongitude) maxLongitude = point.longitude
    }

    return BoundingBox(
        Point(minLatitude - 0.01, minLongitude - 0.01),
        Point(maxLatitude + 0.01, maxLongitude + 0.01)
    )
}

