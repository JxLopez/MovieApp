package com.jxlopez.movieapp.ui.location

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.jxlopez.movieapp.R
import com.jxlopez.movieapp.databinding.FragmentLocationBinding
import com.jxlopez.movieapp.model.LocationUserItem
import com.jxlopez.movieapp.services.LocationService
import com.jxlopez.movieapp.util.Constants.LocationService.ACTION_START_OR_RESUME_SERVICE
import com.jxlopez.movieapp.util.Constants.LocationService.ACTION_STOP_SERVICE
import com.jxlopez.movieapp.util.Constants.LocationService.REQUEST_CODE_LOCATION_PERMISSION
import com.jxlopez.movieapp.util.PermissionsUtility
import com.jxlopez.movieapp.util.components.BaseFragment
import com.jxlopez.movieapp.util.extensions.bitmapDescriptorFromVector
import com.jxlopez.movieapp.util.extensions.observe
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class LocationFragment : BaseFragment(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: FragmentLocationBinding
    private val viewModel: LocationViewModel by viewModels()
    private val markerOptions = MarkerOptions()
    private lateinit var map: GoogleMap
    private lateinit var markerSelect: Marker
    private var listLocations = listOf<LocationUserItem>()
    private val locationAdapter by lazy {
        LocationAdapter()
    }
    private var isTracking = false

    private val callback = OnMapReadyCallback { googleMap ->
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_map_black))
        googleMap.uiSettings.isZoomControlsEnabled = true
        map = googleMap
        map.setPadding(0,0,0,binding.recyclerMaps.height)
        setDataRecycler()
        setListenerMap()
        viewModel.getLocations()
    }
    var mapView: SupportMapFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(inflater)
        observe(viewModel.getViewState(), ::onViewState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapView?.getMapAsync(callback)
        requestPermissions()
        setObservers()
        setListeners()
        updateTracking(LocationService.isTracking.value == true)
    }

    private fun setListeners() {
        binding.btnStartLocation.setOnClickListener {
            runLocation()
        }
    }

    private fun requestPermissions() {
        if(PermissionsUtility.hasLocationPermissions(requireContext())) return
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_location),
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_location),
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    private fun sendCommandToService(action: String) =
        Intent(requireContext(), LocationService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    private fun setObservers() {
        LocationService.isTracking.observe(viewLifecycleOwner) {
            updateTracking(it)
        }

        LocationService.lastLocation.observe(viewLifecycleOwner) {
            it?.let {
                viewModel.saveLocation(it)
            }
        }
    }

    private fun runLocation() {
        if(isTracking) {
            sendCommandToService(ACTION_STOP_SERVICE)
        } else {
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
        }
    }

    private fun updateTracking(isTracking: Boolean) {
        this.isTracking = isTracking
        if(!isTracking) {
            binding.btnStartLocation.text = getString(R.string.start_location)
            binding.tvStatusLocation.text = getString(R.string.sharing_location_share_location)
            binding.ivIconStatusLocation.setImageResource(R.drawable.ic_location_off_24)
        } else {
            binding.btnStartLocation.text = getString(R.string.stop_location)
            binding.tvStatusLocation.text = getString(R.string.sharing_location_description)
            binding.ivIconStatusLocation.setImageResource(R.drawable.ic_location_on_24)
        }
    }

    private fun setDataRecycler() {
        val llm = LinearLayoutManager(context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        binding.recyclerMaps.setHasFixedSize(true)
        binding.recyclerMaps.layoutManager = llm
        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(binding.recyclerMaps)
        binding.recyclerMaps.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val position = llm.findFirstVisibleItemPosition()
                    val item = locationAdapter.getItemByPosition(position)
                    val latLng = LatLng(item.latitude, item.longitude)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f))
                    if(latLng.latitude != 0.0 && latLng.longitude != 0.0)
                        selectMarker(latLng)
                }
            }
        })
    }

    private fun addMarkers() {
        val builder = LatLngBounds.Builder()
        var isAddedPoints = false
        for (i in listLocations) {
            if(i.latitude != 0.0 && i.longitude != 0.0) {
                val latLng = LatLng(i.latitude, i.longitude)
                builder.include(latLng)
                isAddedPoints = true
                markerOptions.position(latLng)
                    .title("Lat: ${i.latitude}, Lng: ${i.longitude}")
                    .icon(
                        bitmapDescriptorFromVector(
                            requireContext(), R.drawable.ic_marker_unselect
                        )
                    )

                markerSelect = map.addMarker(markerOptions)!!
            }
        }
        if(isAddedPoints) {
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 56)
            map.animateCamera(cu)
        }
    }

    private fun selectMarker(latLng: LatLng){
        map.clear()
        for (i in listLocations) {
            val latLngUser = LatLng(i.latitude, i.longitude)
            markerOptions.position(latLngUser)
                .title("Lat: ${i.latitude}, Lng: ${i.longitude}")
                .icon(bitmapDescriptorFromVector(requireContext()
                    , R.drawable.ic_marker_unselect))

            markerSelect = map.addMarker(markerOptions)!!
        }

        markerOptions.position(latLng)
            .icon(bitmapDescriptorFromVector(requireContext()
                , R.drawable.ic_marker))

        markerSelect = map.addMarker(markerOptions)!!

    }
    private fun findMarkerPosByLatLng(latLng: LatLng): Int {
        var pos = 0
        for (i in listLocations) {
            val latLngUser = LatLng(i.latitude, i.longitude)
            if (latLngUser == latLng) {
                pos = listLocations.indexOf(i)
            }
        }
        return pos
    }

    private fun validateSelectMarker() {
        if(listLocations.isNotEmpty()) {
            val default = LatLng(listLocations[0].latitude, listLocations[0].longitude)
            default?.let {
                selectMarker(it)
            }
        }
    }

    private fun setListenerMap() {
        map.setOnMarkerClickListener { marker ->

            val pos = findMarkerPosByLatLng(marker.position)
            val monitor = Runnable {
                binding.recyclerMaps
                    .scrollToPosition(pos)
            }
            Handler().postDelayed(monitor, 1000)

            markerSelect.setIcon(bitmapDescriptorFromVector
                (requireContext(),R.drawable.ic_marker_unselect))

            markerSelect = marker

            markerSelect.setIcon(bitmapDescriptorFromVector(
                requireContext(),R.drawable.ic_marker))

            false
        }

        map.setOnMapClickListener { latLng ->
            markerSelect.setIcon(bitmapDescriptorFromVector
                (requireContext(),R.drawable.ic_marker_unselect))
        }
    }

    private fun onViewState(state: LocationViewState?) {
        when(state) {
            is LocationViewState.Locations -> {
                listLocations = state.locations
                if(state.locations.isNotEmpty()) {
                    locationAdapter.submitList(state.locations)
                    if (this::map.isInitialized) {
                        binding.recyclerMaps.adapter = locationAdapter
                        addMarkers()
                        validateSelectMarker()
                    }
                } else {
                    showError(getString(R.string.message_error_locations))
                }
            }
            null -> {}
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) { }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}