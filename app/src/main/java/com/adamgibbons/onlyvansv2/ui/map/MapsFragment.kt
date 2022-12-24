package com.adamgibbons.onlyvansv2.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.adamgibbons.onlyvansv2.R
import com.adamgibbons.onlyvansv2.databinding.FragmentMapsBinding
import com.adamgibbons.onlyvansv2.helpers.customTransformation
import com.adamgibbons.onlyvansv2.helpers.locationToLatLng
import com.adamgibbons.onlyvansv2.models.VanModel
import com.adamgibbons.onlyvansv2.ui.van.VanDetailViewModel
import com.adamgibbons.onlyvansv2.ui.van.VanListFragmentDirections
import com.bumptech.glide.Glide
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val mapsViewModel : MapsViewModel by activityViewModels()

    private val callback = OnMapReadyCallback { googleMap ->

        mapsViewModel.load()
        mapsViewModel.observableVansList.observe(viewLifecycleOwner, Observer { vans ->
            vans?.let {
                for (v in vans) {

                    googleMap.addMarker(
                        MarkerOptions()
                        .position(locationToLatLng(v.location))
                        .title(v.id)
                        .draggable(true)
                    )

                    googleMap.setOnMarkerClickListener {
                        it.title?.let { it1 -> mapsViewModel.getVan(it1) }

                        true
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        binding.mapVanCard.visibility = View.INVISIBLE

        mapsViewModel.observableVan.observe(viewLifecycleOwner) { van ->
            binding.vm = mapsViewModel
            binding.mapVanCard.visibility = View.VISIBLE
            binding.vanTitle.text = van.title
            binding.vanDescription.text = van.description
            Picasso.get().load(van.imageUri.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.vanThumbnail)

            binding.mapVanCard.setOnClickListener {
                val action = MapsFragmentDirections.actionNavMapToVanDetailFragment(van.id)
                findNavController().navigate(action)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onResume() {
        super.onResume()
        mapsViewModel.load()
    }
}

