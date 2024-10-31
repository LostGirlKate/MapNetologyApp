package ru.lostgirl.mymapapp.ui.markerlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.geometry.Point
import ru.lostgirl.mymapapp.R
import ru.lostgirl.mymapapp.databinding.FragmentMarkerListBinding
import ru.lostgirl.mymapapp.model.Marker
import ru.lostgirl.mymapapp.ui.MainViewModel
import ru.lostgirl.mymapapp.ui.markerlist.adapter.MarkerAdapter
import ru.lostgirl.mymapapp.ui.markerlist.adapter.OnInteractionListener
import ru.lostgirl.mymapapp.utils.hideKeyboardFrom

class MarkerListFragment : Fragment() {
    private lateinit var binding: FragmentMarkerListBinding
    private val viewModel: MainViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val action =
                MarkerListFragmentDirections.actionMarkerListFragmentToMapFragment2(
                    null
                )
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMarkerListBinding.inflate(layoutInflater)
        requireActivity().onBackPressedDispatcher.addCallback(getViewLifecycleOwner(), callback)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = MarkerAdapter(object : OnInteractionListener {
            override fun onEditClick(marker: Marker) {
                showEditMarkerBar(marker)
            }

            override fun onDeleteClick(marker: Marker) {
                viewModel.removeById(marker.id)
            }

            override fun onItemClick(marker: Marker) {
                val action =
                    MarkerListFragmentDirections.actionMarkerListFragmentToMapFragment2(
                        marker
                    )
                findNavController().navigate(action)
            }
        })
        binding.rcmarkers.adapter = adapter
        viewModel.state.observe(viewLifecycleOwner) {
            adapter.submitList(it.markerList)
        }
        binding.buttonCloseBottomInfo.setOnClickListener {
            binding.bottomInfo.visibility = View.GONE
            hideKeyboardFrom(requireContext(), binding.addMarkTitle)
        }
    }

    private fun showEditMarkerBar(marker: Marker) = with(binding) {
        newMarkCoords.text =
            resources.getString(R.string.coords, marker.latitude.toString(), marker.longitude.toString())
        buttonAddMarker.setOnClickListener {
            val description = newMarkerTitle.text.toString()
            viewModel.save(description, Point(marker.latitude, marker.longitude), marker.id)
            hideKeyboardFrom(requireContext(), addMarkTitle)
            binding.bottomInfo.visibility = View.GONE
        }
        addMarkTitle.text = resources.getString(R.string.edit_marker)
        newMarkerTitle.setText(marker.description)
        addMarkBar.visibility = View.VISIBLE
        bottomInfo.visibility = View.VISIBLE
    }
}