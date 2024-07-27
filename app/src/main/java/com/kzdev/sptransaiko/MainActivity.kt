package com.kzdev.sptransaiko

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.kzdev.sptransaiko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var mMap: GoogleMap

    val location = LatLng(-23.5505, -46.6333)

    private var locationArrayList: ArrayList<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val map = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        map.getMapAsync(this)

        locationArrayList = ArrayList()

        locationArrayList!!.add(location)

    }

    override fun onMapReady(googlemap: GoogleMap) {

        mMap = googlemap

        for (i in locationArrayList!!.indices){
            mMap.addMarker(MarkerOptions().position(locationArrayList!![i]). title("Marker"))
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList!!.get(i)))
        }

    }
}