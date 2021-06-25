package com.mj.aop_part4_chapter03

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.mj.aop_part4_chapter03.databinding.ActivityMapBinding
import com.mj.aop_part4_chapter03.model.SearchResultEntity

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var searchResult: SearchResultEntity
    private lateinit var map: GoogleMap
    private var currentSelectMarker: Marker? = null

    companion object{
        const val SEARCH_RESULT_EXTRA_KEY = "SEARCH_RESULT_EXTRA_KEY"
        const val CAMERA_ZOOM_LEVEL = 17f
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(::searchResult.isInitialized.not()){
            intent?.let {
                searchResult = it.getParcelableExtra<SearchResultEntity>(SEARCH_RESULT_EXTRA_KEY) ?: throw Exception("no data")
                setupGoogleMap()
            }
        }

    }

    private fun setupGoogleMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        currentSelectMarker = setupMarker(searchResult)
        currentSelectMarker?.showInfoWindow()
    }

    private fun setupMarker(searchResultEntity: SearchResultEntity): Marker {
        val positionLatLng = LatLng(
            searchResultEntity.locationLatLng.latitude.toDouble(),
            searchResultEntity.locationLatLng.longitude.toDouble())

        val markerOptions = MarkerOptions().apply {
            position(positionLatLng)
            title(searchResultEntity.name)
            snippet(searchResultEntity.fullAddress)
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(positionLatLng, CAMERA_ZOOM_LEVEL))

        return map.addMarker(markerOptions)
    }
}