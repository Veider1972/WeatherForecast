package ru.veider.weatherforecast.ui.map

import android.annotation.SuppressLint
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.runtime.image.ImageProvider
import kotlinx.android.synthetic.main.weather_fragment.yandex_map
import ru.veider.weatherforecast.R
import ru.veider.weatherforecast.databinding.MapFragmentBinding
import ru.veider.weatherforecast.repository.weather.WeatherQuery
import ru.veider.weatherforecast.ui.utils.showToast

class MapFragment(private val startPoint: WeatherQuery) : Fragment() {

    private var _binder: MapFragmentBinding? = null
    private val binder get() = _binder!!
    private var lastPoint: Point? = null
    private var mapMode = MapMode.MOVE

    private var inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            when (mapMode) {
                MapMode.PIN -> {
                    map.mapObjects.addPlacemark(point, ImageProvider.fromResource(requireContext(),
                                                                                  R.drawable.map_pin))
                }
                MapMode.ROUTE -> {
                    lastPoint?.let {
                        map.mapObjects.addPolyline(Polyline(arrayListOf(lastPoint, point)))
                    }
                    lastPoint = point
                    map.mapObjects.addPlacemark(point, ImageProvider.fromResource(requireContext(),
                                                                                  R.drawable.map_marker))
                }
            }
        }

        override fun onMapLongTap(map: Map, point: Point) {}
    }

    companion object {
        @JvmStatic
        fun newInstance(startPoint: WeatherQuery) = MapFragment(startPoint)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(requireContext())
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
                             ): View {
        _binder = MapFragmentBinding.inflate(inflater)
        with(binder) {
            yandexMap.apply {
                map.apply {
                    addInputListener(inputListener)
                    move(CameraPosition(Point(startPoint.latitude, startPoint.longitude), 11.0f,
                                        0.0f, 0.0f), Animation(Animation.Type.SMOOTH, 0.0f), null)
                }
            }

            searchButton.setOnClickListener {
                val searchString = searchEditText.text.trim().toString()
                if (searchString.isEmpty()) {
                    requireContext().showToast(getString(R.string.empty_adress_message))
                } else {
                    val geoCoder = Geocoder(requireContext())
                    val seekPoints = geoCoder.getFromLocationName(searchString, 1)
                    yandexMap.map.move(
                            CameraPosition(Point(seekPoints[0].latitude, seekPoints[0].longitude),
                                           17.0f, 0.0f, 0.0f),
                            Animation(Animation.Type.SMOOTH, 0.0f), null)
                }
            }
            zoomInButton.setOnClickListener {
                val camera = yandexMap.map.cameraPosition
                yandexMap.map.move(
                        CameraPosition(camera.target, camera.zoom + 1, camera.azimuth, camera.tilt))
            }
            zoomOutButton.setOnClickListener {
                val camera = yandexMap.map.cameraPosition
                yandexMap.map.move(
                        CameraPosition(camera.target, camera.zoom - 1, camera.azimuth, camera.tilt))
            }
            moveButton.apply {
                backgroundTintList = resources.getColorStateList(R.color.selected)
                setOnClickListener {
                    mapMode = MapMode.MOVE
                    moveButton.backgroundTintList = resources.getColorStateList(R.color.selected)
                    pinButton.backgroundTintList = zoomInButton.backgroundTintList
                    routeButton.backgroundTintList = zoomInButton.backgroundTintList
                }
            }
            pinButton.setOnClickListener {
                mapMode = MapMode.PIN
                pinButton.backgroundTintList = resources.getColorStateList(R.color.selected)
                moveButton.backgroundTintList = zoomInButton.backgroundTintList
                routeButton.backgroundTintList = zoomInButton.backgroundTintList
            }
            routeButton.setOnClickListener {
                mapMode = MapMode.ROUTE
                routeButton.backgroundTintList = resources.getColorStateList(R.color.selected)
                pinButton.backgroundTintList = zoomInButton.backgroundTintList
                moveButton.backgroundTintList = zoomInButton.backgroundTintList
            }
        }
        return binder.root
    }

    override fun onStop() {
        yandex_map.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        yandex_map.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binder = null
    }

    enum class MapMode {
        PIN, ROUTE, MOVE
    }
}