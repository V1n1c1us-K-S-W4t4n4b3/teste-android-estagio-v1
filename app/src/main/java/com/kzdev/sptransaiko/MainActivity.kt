package com.kzdev.sptransaiko

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.kzdev.sptransaiko.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var mMap: GoogleMap

    private val location = LatLng(-23.5505, -46.6333)

    private var locationArrayList: ArrayList<LatLng>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val map = supportFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
        map.getMapAsync(this)

        locationArrayList = ArrayList()

        locationArrayList!!.add(location)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val spBounds = LatLngBounds(
            LatLng(-23.8136, -46.7553),
            LatLng(-23.4377, -46.2920)
        )

        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.icon_stop_buss)
        val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 100, 100, false)
        val customIcon = BitmapDescriptorFactory.fromBitmap(scaledBitmap)

        for (i in locationArrayList!!.indices) {
            mMap.addMarker(
                MarkerOptions()
                    .position(locationArrayList!![i])
                    .title("Marker")
                    .icon(customIcon)
            )
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(spBounds, 0))
    }
}