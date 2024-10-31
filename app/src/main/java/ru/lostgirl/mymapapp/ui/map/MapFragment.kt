package ru.lostgirl.mymapapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import ru.lostgirl.mymapapp.R
import ru.lostgirl.mymapapp.databinding.FragmentMapBinding
import ru.lostgirl.mymapapp.model.Marker
import ru.lostgirl.mymapapp.ui.MainViewModel


class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var map: Map
    private val viewModel: MainViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val inputListener = object : InputListener {
        override fun onMapLongTap(map: Map, point: Point) {

        }

        override fun onMapTap(map: Map, point: Point) = Unit
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        MapKitFactory.initialize(context)
        binding = FragmentMapBinding.inflate(layoutInflater)
        map = binding.mapview.mapWindow.map
        map.addInputListener(inputListener)
        val placemarkTapListener = MapObjectTapListener { mapObject, point ->
            Toast.makeText(
                context,
                "${mapObject.userData} \n (${point.longitude}, ${point.latitude})",
                Toast.LENGTH_SHORT
            ).show()
            true
        }
        viewModel.state.observe(viewLifecycleOwner) {
            map.mapObjects.clear()
            it.markerList.forEach { marker ->

             val placemarkMapObject  =   map.mapObjects.addPlacemark().apply {
                    geometry = marker.point
                    setIcon(ImageProvider.fromResource(context, R.drawable.ic_dollar_pin))
                    userData = marker.description
                }
                placemarkMapObject.addTapListener(placemarkTapListener)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapview.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        binding.mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onSaveInstanceState(outState)
    }

    private fun moveToPoint(marker: Marker) {
        map.move(
            CameraPosition(
                marker.point,
                /* zoom = */ 17.0f,
                /* azimuth = */ 150.0f,
                /* tilt = */ 30.0f
            )
        )
    }

    companion object {
        private val SMOOTH_ANIMATION = Animation(Animation.Type.SMOOTH, 0.4f)
        private val START_POSITION = CameraPosition(Point(54.707590, 20.508898), 15f, 0f, 0f)

    }

}