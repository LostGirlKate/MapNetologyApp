package ru.lostgirl.mymapapp.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import ru.lostgirl.mymapapp.R
import ru.lostgirl.mymapapp.databinding.FragmentMapBinding
import ru.lostgirl.mymapapp.model.Marker
import ru.lostgirl.mymapapp.ui.MainViewModel
import ru.lostgirl.mymapapp.utils.hideKeyboardFrom

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var map: Map
    private val viewModel: MainViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )
    private val args: MapFragmentArgs by navArgs()

    private val inputListener = object : InputListener {
        override fun onMapLongTap(map: Map, point: Point) {
            showAddMarkerBar(point)
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
        binding.buttonShowAllMarks.setOnClickListener {
            findNavController().navigate(R.id.action_mapFragment2_to_markerListFragment)
        }
        binding.buttonCloseBottomInfo.setOnClickListener {
            binding.bottomInfo.visibility = View.GONE
            hideKeyboardFrom(requireContext(), binding.addMarkTitle)
        }
        val placemarkTapListener = MapObjectTapListener { mapObject, point ->
            showMarkInfo(marker = (mapObject.userData as Marker))
            true
        }
        viewModel.state.observe(viewLifecycleOwner) {
            map.mapObjects.clear()
            it.markerList.forEach { marker ->

                val placemarkMapObject = map.mapObjects.addPlacemark().apply {
                    geometry = Point(marker.latitude, marker.longitude)
                    setIcon(ImageProvider.fromResource(context, R.drawable.ic_dollar_pin))
                    userData = marker
                }
                placemarkMapObject.addTapListener(placemarkTapListener)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (args.marker != null) {
            val focusMarker = args.marker
            focusMarker?.let { showMarkInfo(focusMarker) }
        }
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
                Point(marker.latitude, marker.longitude),
                17.0f,
                150.0f,
                30.0f
            )
        )
    }

    private fun showMarkInfo(marker: Marker) = with(binding) {
        markTitle.text = marker.description
        markCoords.text =
            resources.getString(R.string.coords, marker.latitude.toString(), marker.longitude.toString())
        buttonDelMark.setOnClickListener {
            viewModel.removeById(marker.id)
            binding.bottomInfo.visibility = View.GONE
        }
        buttonEditMark.setOnClickListener {
            showEditMarkerBar(marker)
        }
        addMarkBar.visibility = View.GONE
        markInfo.visibility = View.VISIBLE
        bottomInfo.visibility = View.VISIBLE
        moveToPoint(marker)
    }

    private fun showAddMarkerBar(point: Point) = with(binding) {
        newMarkCoords.text = resources.getString(R.string.coords, point.longitude.toString(), point.latitude.toString())
        buttonAddMarker.setOnClickListener {
            val description = newMarkerTitle.text.toString()
            viewModel.save(description, point)
            binding.bottomInfo.visibility = View.GONE
            hideKeyboardFrom(requireContext(), addMarkTitle)
        }
        addMarkTitle.text = resources.getString(R.string.add_marker)
        newMarkerTitle.text.clear()
        addMarkBar.visibility = View.VISIBLE
        markInfo.visibility = View.GONE
        bottomInfo.visibility = View.VISIBLE
    }

    private fun showEditMarkerBar(marker: Marker) = with(binding) {
        newMarkCoords.text =
            resources.getString(R.string.coords, marker.latitude.toString(), marker.longitude.toString())
        buttonAddMarker.setOnClickListener {
            val description = newMarkerTitle.text.toString()
            viewModel.save(description, Point(marker.latitude, marker.longitude), marker.id)
            binding.bottomInfo.visibility = View.GONE
            hideKeyboardFrom(requireContext(), addMarkTitle)
        }
        addMarkTitle.text = resources.getString(R.string.edit_marker)
        newMarkerTitle.setText(marker.description)
        addMarkBar.visibility = View.VISIBLE
        markInfo.visibility = View.GONE
        bottomInfo.visibility = View.VISIBLE
    }
}